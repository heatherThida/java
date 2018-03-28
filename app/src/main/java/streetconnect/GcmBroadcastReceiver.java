package streetconnect;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

// this shouldn't need to be altered anymore. Mostly stock android code required to implement notifications.

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("error", "onReceive entered");
        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);

        if(Notifications.notifs==null)
        {
            Notifications.notifs=new Notifications();
        }

        Notifications.notifs.onNavigationDrawerItemSelected(0);
       // Notifications.items.add("temo data");
                Bundle extras = intent.getExtras();
        String temp = extras.getString("Title");
         Notifications.items.add(temp);
        String detail = extras.getString("Message");
        Notifications.messages_detail.add(detail);


    }
}