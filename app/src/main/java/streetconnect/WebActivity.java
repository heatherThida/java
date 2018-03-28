package streetconnect;

// Sample Google Forms survey at http://goo.gl/forms/UW7q1vM6jc

// Displays the surveys in a WebActivity in-app.

// TODO:
// 1. Receive the specific survey address programmatically (get ID # and then query database? Receive address directly?)
// 2. Then change the loadUrl line in the onCreate method to reflect this.

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import streetconnect.streetconnect.R;


public class WebActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Bundle extras = getIntent().getExtras();
        String survey = getIntent().getExtras().getString("survey");
        Log.d("webactivity", "survey: " + survey);

        WebView webview = new WebView(this);
        setContentView(webview);

        // Let's display the progress in the activity title bar, like the
        // browser app does.
        //getWindow().requestFeature(Window.FEATURE_PROGRESS);

        webview.getSettings().setJavaScriptEnabled(true);

        final Activity activity = this;
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 1000);
            }
        });
        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });

        if(survey != null)
        {
            webview.loadUrl(survey); // used to be a dummy variable.
        }
        else
        {
            // get context first
            Context context = getApplicationContext();

            // works with strings as well
            CharSequence text = "Web Address is null, sorry.";

            // options are short or long
            int duration = Toast.LENGTH_LONG;

            Toast.makeText(context, text, duration).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
