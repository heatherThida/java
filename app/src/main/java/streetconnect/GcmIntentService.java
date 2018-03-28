package streetconnect;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import java.util.Random;
import streetconnect.streetconnect.R;

public class GcmIntentService extends IntentService
{
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService()
    {
        super("GcmIntentService");
    }
    @Override
    protected void onHandleIntent(Intent intent)
    {
        Bundle extras = intent.getExtras();
        // quick test log to see what the string looks like:
        Log.d("Extras: ", extras.toString());
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);
        Log.d("error", "onHandleIntent entered");

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */

            Log.d("error", "!extras.isEmpty() entered");
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType))
            {
                sendNotification("Send error: ", " ", " ", " ", " ");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType))
            {
                sendNotification("Deleted messages on server: ", " ", " ", " ", " ");

                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
            {
                // parse notification here
                String message = extras.getString("Message");
                String title = extras.getString("Title");
                String subtitle = extras.getString("Subtitle");
                String survey = extras.getString("Survey");
                String ticker = extras.getString("TickerText");
                String address = extras.getString("Location"); // according to Edmond.
                Log.d("extras help", extras.toString());
                Log.d("extras help contents", extras.describeContents() + " ");

                // need to have some kind of recognition of the different types of data here.

                // add notification to database here.
                SQLiteDatabase db = new DBHelper(this).getWritableDatabase();
                ContentValues newValues = new ContentValues();
                newValues.put(DBHelper.MESSAGE_COLUMN, message);
                newValues.put(DBHelper.TITLE_COLUMN, title);
                newValues.put(DBHelper.SUBTITLE_COLUMN, subtitle);
                newValues.put(DBHelper.SURVEY_COLUMN, survey);
                newValues.put(DBHelper.ADDRESS_COLUMN, address);
                db.insert(DBHelper.DATABASE_TABLE, null, newValues);

                // Post notification of received message.
                sendNotification(message, title, subtitle, survey, ticker);
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String message, String title, String subtitle, String survey, String ticker)
    {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Context context = getApplicationContext();
        Intent notificationIntent = new Intent(context, Home.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                //.setCategory(Notification.CATEGORY_PROMO) // API only allowed in 21+
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_action_email)
                .setAutoCancel(true)
                //.setVisibility(visibility)
                .addAction(android.R.drawable.ic_menu_view, "View details", contentIntent)
                .setDefaults(
                        Notification.DEFAULT_SOUND
                                | Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // this needs to be changed out of a random integer and into something more procedural.
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(m, notification);
     }
}