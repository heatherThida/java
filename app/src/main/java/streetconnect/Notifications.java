package streetconnect;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import streetconnect.streetconnect.R;

// This is the app's default screen and its bread and butter. This contains a ListView with recent notifications.
// Upon tapping any notification, a DetailActivity should load with the entire notification and some sort of optional maps view.

// TODO(kfcampbell):

// NOTE: for now, streetconnect gray color is #E8E8E8

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Notifications#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Notifications extends Fragment implements NavigationDrawerFragment.NavigationDrawerCallbacks
{
    // I think the following parameters code is unnecessary.
    // TODO(kfcampbell): get rid of the parameters code
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    static ArrayAdapter<String> adapter;
    static ListView listView;
    static List<String> items;
    static List<String> messages_detail;
    static List<String> addresses;
    static List<String> surveys;
    static Notifications notifs;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Notifications.
     */
    // TODO: Rename and change types and number of parameters
    public static Notifications newInstance(String param1, String param2) {

        Notifications fragment;
//        if(Notifications.notifs==null) {
//             fragment = new Notifications();
//        }
        if(notifs==null) {
            notifs = new Notifications();
        }
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        notifs.setArguments(args);
        return notifs;
    }

    public Notifications() {
        // Required empty public constructor
    }

    public static Notifications getOldInstance()
    {
        return notifs;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    // function to populate ListView with notifications already received.
    private void displayListView()
    {
        // get fragment context
        Context context = getActivity().getApplicationContext();

        // initalize arraylists to hold the items and their relevent details.
        items = new ArrayList<String>();
        messages_detail = new ArrayList<String>();
        addresses = new ArrayList<String>();
        surveys = new ArrayList<String>();

        // test of looking up database to display in listView.
        SQLiteDatabase db = new DBHelper(context).getReadableDatabase();
        items.clear();

        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        final String[] resultColumns = {DBHelper.ID_COLUMN, DBHelper.MESSAGE_COLUMN, DBHelper.TITLE_COLUMN, DBHelper.SURVEY_COLUMN, DBHelper.ADDRESS_COLUMN};
        final Cursor cursor = db.query(DBHelper.DATABASE_TABLE, resultColumns, where, whereArgs, groupBy, having, order);

        // description of cursor strings:
        // 0. ?
        // 1. message body
        // 2. title
        // 3. survey/google form
        // 4. address

        // hack to make whole row selectable. implemented because the custom row is no longer being used.
        String empty = "                                                                            ";

        while (cursor.moveToNext())
        {
            String note = cursor.getString(2); // note is the title here
            if(note.isEmpty() || note == "")
            {
                // if the title is blank but the message has content, display it with the "New StreetConnect Message" prefix
                items.add("New StreetConnect Message" + empty);
            }
            else
            {
                // otherwise display the regular title
                items.add(note + empty);
            }

            String msg_detail = cursor.getString(1);
            messages_detail.add(msg_detail);

            // pull data for addresses and surveys
            String address_detail = cursor.getString(4); // 4 is location.
            addresses.add(address_detail);

            String survey_detail = cursor.getString(3); // 3 is the survey.
            surveys.add(survey_detail);
        }

        // for some reason this fires twice instead of just once when first going to notifications
        if(items.isEmpty())
        {
            // commented to avoid log spam
            //Log.d("empty message", "no notifications found at all");

            // 1. Instantiate an AlertDialog.Builder with its constructor
            // test of custom AlertDialog theme. to return to the way it was, delete the below line and uncomment the other line.
            ContextThemeWrapper ctw = new ContextThemeWrapper(getActivity(), R.style.AlertDialogTheme);

            //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            AlertDialog.Builder builder = new AlertDialog.Builder(ctw);

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Come back here once a message has arrived in order to read it!" +
                    "\nOtherwise go to Settings to verify you're registered with StreetConnect.").setTitle("No Messages Currently Available");

            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    // need a better message here eventually. commented to avoid log spam.
                    //Log.d("empty message", "user clicked okay button");
                }
            });


            // Get the AlertDialog from create()
            AlertDialog dialog = builder.create();

            dialog.show();
        }

        //reversing the order of elements in list, to show the latest notification on top.
        Collections.reverse(items);
        Collections.reverse(messages_detail);
        Collections.reverse(addresses);
        Collections.reverse(surveys);

        // could/should potentially go above the reverse ordering lines.
        cursor.close();
        db.close();

        // instantiate the ListView. Despite the warning, will not return null.
        listView = (ListView) getView().findViewById(R.id.listView); // used to be android.R.layout.simple
        adapter = new ArrayAdapter<String>(context, R.layout.sample_textview, items); // need to change simple_list_item1 in order to customize things
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getActivity().getBaseContext(), DetailActivity.class);

                // load up bundle to pass to DetailActivity
                Bundle extras = new Bundle();
                extras.putInt("ID", position);
                extras.putInt("total", listView.getAdapter().getCount());

                String msg = messages_detail.get(position);//added for test - Praneet
                String title = items.get(position);
                String address = addresses.get(position); // added to get the address
                String survey = surveys.get(position); // added to get the survey

                extras.putString("message_detail", msg);
                extras.putString("title", title);
                extras.putString("address", address);
                extras.putString("survey", survey); // should be the variable survey. could be a dummy web address
                intent.putExtras(extras);
                startActivity(intent);
            }

        });

        registerForContextMenu(listView);
    }

    // unused...rough draft of code currently implemented in DetailActivity.java.
    private void deleteItem(int row, int id)
    {
        // useful stackoverflow page: http://stackoverflow.com/questions/7510219/deleting-row-in-sqlite-in-android

        // get fragment context
        Context context = getActivity().getApplicationContext();

        // test deletion code
        SQLiteDatabase db1 = new DBHelper(context).getWritableDatabase(); // open the database
        String table = DBHelper.DATABASE_TABLE; // set the correct table here
        String whereClause = "_id" + "=?"; // get id of item here. row + id need to be programmatically selected.

        String[] whereArgs = new String[]{String.valueOf(row)}; // not quite sure of the significance of this
        db1.delete(table, whereClause, whereArgs);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        // display ListView here because of lifecycle issues when placed in OnCreate or OnCreateView
        displayListView();

        super.onActivityCreated(savedInstanceState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

   public void onNavigationDrawerItemSelected(int position)
    {
        NavigationDrawerFragment ndf = new NavigationDrawerFragment();
      //  Notifications fragment = new Notifications();
        FragmentManager fragmentManager = getFragmentManager();
        if(fragmentManager!=null) {
            fragmentManager.beginTransaction().replace(R.id.container, Notifications.notifs).commit();
        }
        Notifications.listView.setAdapter(Notifications.adapter);
        Notifications.adapter.notifyDataSetChanged();
        listView.invalidateViews();
        listView.invalidateViews();
        listView.invalidateViews();

    }
}