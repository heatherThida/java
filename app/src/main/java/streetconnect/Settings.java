package streetconnect;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import streetconnect.streetconnect.R;

// Depending on how we decide to implement user-defined settings, I'm thinking this could offer a way to edit
// the phone's sharedPrefs file to store each user's settings. It could also provide a way for each user to determine
// which service providers to subscribe to if we decide to go that route.


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Settings.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Settings extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String regid;
    Context context;
    GoogleCloudMessaging gcm;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    String SENDER_ID = "305990959301";

    private OnFragmentInteractionListener mListener;

    private boolean registeredStreetConnect = false;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Settings.
     */
    // TODO: Rename and change types and number of parameters
    public static Settings newInstance(String param1, String param2) {
        Settings fragment = new Settings();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Settings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //TextView settings = (TextView) findViewById(R.id.settingsText);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Button registerButton = (Button) view.findViewById(R.id.registerButton);
        TextView settingsText = (TextView) view.findViewById(R.id.settingsText);

        registerButton.setText("Force Registration with StreetConnect");
        settingsText.setText("Click this button if your organization has asked you to register" +
                " or re-register your phone with StreetConnect.  " +
                "Also click if your organization sent you messages, but you didn't receive them.");
        settingsText.setGravity(Gravity.CENTER);

        registerButton.setOnClickListener(new View.OnClickListener()  // View.OnClickListener?
        {
            @Override
            public void onClick(View v)
            {
                //checkIfRegistered();
                registerInBackground();
                //Toast.makeText(getActivity(), "Button Clicked!", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    public void checkIfRegistered()
    {
        context = getActivity().getApplicationContext();

        // Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
        if (checkPlayServices())
        {
            //Log.d("TEST: ", "checkPlayServices() has been entered");
            gcm = GoogleCloudMessaging.getInstance(this.getActivity());
            regid = getRegistrationId(context);

            if (regid.isEmpty())
            {
                Log.d("registration", "registering in background");
                registerInBackground();
            }
            else
            {
                Log.d("=======================", "=======================");
                Log.d("regid", regid);
                Log.d("registration", "regid = " + regid);
                Log.d("=======================", "=======================");
            }
        }
    }

    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getActivity());
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this.getActivity(), PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("TAG: ", "This device is not supported.");
                //finish();
            }
            return false;
        }
        return true;
    }

    private void registerInBackground()
    {
        Log.d("registration", "registerInBackground entered");
        new AsyncTask<Void, Void, String>()
        {
            @Override
            protected String doInBackground(Void... params)
            {
                String msg = "";
                try
                {
                    if (gcm == null)
                    {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }

                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    Log.d("TEST ", "device registered successfully");
                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    // NEED TO IMPLEMENT THE BELOW FUNCTION IN ORDER TO REGISTER EACH APP WITH OUR SERVERS
                    //Log.d("registration", "about to send registration ID to backend");
                    sendRegistrationIdToBackend();

                    // Persist the registration ID - no need to register again.
                    //storeRegistrationId(context, regid); uncomment to get error again.
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg)
            {
                //mDisplay.append(msg + "\n");
            }
        }.execute(null, null, null);
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i("Regid", "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i("Regid", "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion)
        {
            Log.i("Regid", "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context)
    {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getActivity().getSharedPreferences(DetailActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private void sendRegistrationIdToBackend()
    {
        // this is going to cause issues because
        // TODO(kfcampbell): fix this with the updated backend script
        Log.d("registration", "sendRegistrationIdToBacked is entered");
        //String url = "http://sc.tziptzap.com/labrat/streetconnect/mobile/android_verifyregistrationid.php";
        String url = "https://streetconnect.ctagroup.org/mobile/android_getregistrationid.php";

        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        TelephonyManager tMgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String phone_number = tMgr.getLine1Number();


        try
        {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("phone_number", phone_number));
            nameValuePairs.add(new BasicNameValuePair("regid", regid));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            Log.d("registration", "phone number " + phone_number + ", regid: " + regid);

            // Execute HTTP Post Request
            Log.d("registration", "about to try an httpresponse");
            HttpResponse response = httpclient.execute(httppost);
            Log.d("registration", "httpresponse executed");
            Log.d("registration", "httpresponse: " + response.toString());
            StatusLine code = response.getStatusLine();
            Log.d("registration", "status code: " + code.getStatusCode());

            // blocking this off for later usage
            if(code.getStatusCode() == 200)
            {
                Toast.makeText(getActivity(), "Response is 200!", Toast.LENGTH_LONG).show();
            }
            else if(code.getStatusCode() == 404)
            {
                Toast.makeText(getActivity(), "Response is 404!", Toast.LENGTH_LONG).show();
            }

        }
        catch (ClientProtocolException e)
        {
            // TODO Auto-generated catch block
            Log.d("registrationCP", "error: " + e.toString());

        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            Log.d("registrationIO", "error: " + e.toString());
        }

        // END
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
