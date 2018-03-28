package streetconnect;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import streetconnect.streetconnect.R;

// This fragment is meant to display a list of service providers that use StreetConnect, and ideally offer some sort of one-tap signup
// method to receive that provider's notifications. Currently, it's blank and full of only boilerplate code.
// Currently left blank because it's not one of the more pressing priorities.

// TODO(kcampbell):
// 1. Implement a ListView
// 2. Fill it with dummy service providers.
// 3. Maybe experiment with a button to sign up for each provider?
// Fix bug causing ListView to not show up.

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Organizations.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Organizations#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Organizations extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Organizations.
     */
    // TODO: Rename and change types and number of parameters
    public static Organizations newInstance(String param1, String param2) {
        Organizations fragment = new Organizations();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Organizations() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // data here isn't relevant. uses the Information class when another class should be created.
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_organizations, container, false);

        final List<Providers> info = new ArrayList<>();
        info.add(new Providers("149 N. White Road", "Shelter", "Founded in 1979.", "Test Provider 1"));
        info.add(new Providers("149 N. White Road", "Shelter", "Founded in 1979.", "Test Provider 2"));
        info.add(new Providers("149 N. White Road", "Shelter", "Founded in 1979.", "Test Provider 3"));
        info.add(new Providers("149 N. White Road", "Shelter", "Founded in 1979.", "Test Provider 4"));
        info.add(new Providers("149 N. White Road", "Shelter", "Founded in 1979.", "Test Provider 5"));
        info.add(new Providers("149 N. White Road", "Shelter", "Founded in 1979.", "Test Provider 6"));
        info.add(new Providers("149 N. White Road", "Shelter", "Founded in 1979.", "Test Provider 7"));
        info.add(new Providers("149 N. White Road", "Shelter", "Founded in 1979.", "Test Provider 8"));
        info.add(new Providers("149 N. White Road", "Shelter", "Founded in 1979.", "Test Provider 9"));
        info.add(new Providers("149 N. White Road", "Shelter", "Founded in 1979.", "Test Provider 10"));


        // instantiate the ListView here
        ListView listView = (ListView) view.findViewById(R.id.providerListView); //getView().findViewById(R.id.listView);
        listView.setAdapter(new ProviderAdapter(getActivity(), R.layout.provider_row, info));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // pass details into the detail activity (later add map fragment option?)
                Providers providers = info.get(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Bundle extras = new Bundle();
                extras.putString("location", providers.getLocation());
                extras.putString("type", providers.getType());
                extras.putString("shortdescription", providers.getShortDescription());
                extras.putString("providerName", providers.getName());
                intent.putExtras(extras);

                startActivity(intent);

            }
        });
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_organizations, container, false);
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
