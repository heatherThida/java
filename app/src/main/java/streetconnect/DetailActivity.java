package streetconnect;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import streetconnect.streetconnect.R;

// TODO(kfcampbell):
// 2. Update the UI of the DetailActivity to be more complete and display all types of data plus the short summary.
// 4. Add capability to see address of service location on a map fragment.
// 9. Display title on menu bar.

// Link for WebView for sample Google Form: http://goo.gl/forms/UW7q1vM6jc
// Test address (for SAP center): @37.332799,-121.90121,17z


public class DetailActivity extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // get intent and set name and filename variables
        String shortDescription = getIntent().getExtras().getString("shortDescription");
        String providerName = getIntent().getExtras().getString("providerName");
        final String address = getIntent().getExtras().getString("address");
        final String survey = getIntent().getExtras().getString("survey");


        // for the future: get the row number here and delete the correct item from the database.
        int idNumber = getIntent().getExtras().getInt("ID"); // this is the actual row number.
        Log.d("deletion", "id number: " + idNumber);
        int total = getIntent().getExtras().getInt("total");
        Log.d("total amount: ", " " + total);

        // for future reference, the rough draft delete code is in deleteItem in Notifications.java

        if(providerName != null)
        {
            // set name of person
            TextView title = (TextView) findViewById(R.id.fromField);
            title.setText(providerName);
            setTitle(providerName);
        }
        else
        {
            TextView title = (TextView) findViewById(R.id.fromField);
            title.setText("No Service Provider Name");
            setTitle("Notification View");
        }
        if(shortDescription != null)
        {
            // find description tag
            TextView description = (TextView) findViewById(R.id.content);
            description.setText(shortDescription);
        }
        else
        {
            Log.d("TAG: ", "Short description is null...");
            TextView description = (TextView) findViewById(R.id.content);
            description.setText("No description given.");
        }

        Log.d("notifStrings", "Address received in DetailActivity: " + address);
        Log.d("notifStrings", "Survey received in DetailActivity: " + survey);

        //temp setting message detail text on desc label
        TextView description = (TextView) findViewById(R.id.content);
        description.setText(getIntent().getExtras().getString("message_detail"));

        // needs to include a bundle here to pass survey data into the WebActivity
        Button rsvpButton = (Button) findViewById(R.id.rsvpButton);
        if(survey != null && survey.length() > 5)
        {
            rsvpButton.setVisibility(View.VISIBLE);
        }
        else
        {
            rsvpButton.setVisibility(View.INVISIBLE);
        }

        rsvpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // start the WebView for the survey form here
                Bundle extras = new Bundle();
                extras.putString("survey", survey);

                Intent intent = new Intent(DetailActivity.this, WebActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        Button mapsButton = (Button) findViewById(R.id.mapsButton);
        if(address != null && address.length() > 5) // attempt to hide empty surveys.
        {
            mapsButton.setVisibility(View.VISIBLE);
        }
        else
        {
            mapsButton.setVisibility(View.INVISIBLE);
        }
        // need to include a bundle here to pass the address into the MapsActivity
        mapsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // start the Google Maps Activity. In the future, pass in a unique address here.
                Bundle extras = new Bundle();
                extras.putString("address", address);

                Intent intent = new Intent(DetailActivity.this, MapsActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    // in order to reactivate this, head over to menu_detail.xml in /res/menu/
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // there's gotta be a better way to do this, but the trash button's id is 2131624040
        if(id == 2131624040)
        {
            int idNumber = getIntent().getExtras().getInt("ID"); // this is the actual row number.

            // get fragment context
            Context context = getApplicationContext();

            // works with strings as well
            CharSequence text = "Message Deleted: " + idNumber;

            // options are short or long
            int duration = Toast.LENGTH_LONG;

            Toast.makeText(context, text, duration).show();

            // now we actually gotta delete the thing
            // useful stackoverflow page: http://stackoverflow.com/questions/7510219/deleting-row-in-sqlite-in-android

            int total = getIntent().getExtras().getInt("total");
            int altTotal = total-idNumber;

            // test deletion code. currently deletes the last option instead of the 1st I think...
            SQLiteDatabase db1 = new DBHelper(context).getWritableDatabase(); // open the database
            String table = DBHelper.DATABASE_TABLE; // set the correct table here
            String whereClause = "_id" + "=?" + (altTotal); // get id of item here. row + id need to be programmatically selected.

            String[] whereArgs = new String[]{String.valueOf(/*row*/altTotal)}; // not quite sure of the significance of this
            db1.delete(table, whereClause, whereArgs);

            db1.close();

            Log.d("deletion", "database closed");
            Log.d("deletion", "total = " + total);
            Log.d("deletion", "id = " + idNumber);
            Log.d("deletion", "total-id = " + altTotal);
            Log.d("deletion", "whereClause=" + whereClause);
            Log.d("deletion", "whereArgs=" + Arrays.toString(whereArgs));
        }

        return super.onOptionsItemSelected(item);
    }
}
