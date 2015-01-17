package nmct.howest.be.rideshare.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nmct.howest.be.rideshare.Helpers.APIHelper;
import nmct.howest.be.rideshare.Helpers.Utils;
import nmct.howest.be.rideshare.Loaders.Json.TripLoader;
import nmct.howest.be.rideshare.Models.Match;
import nmct.howest.be.rideshare.Models.Trip;
import nmct.howest.be.rideshare.R;
import nmct.howest.be.rideshare.RideshareApp;

public class DetailSavedTripFragment extends Fragment implements LoaderManager.LoaderCallbacks<Trip> {

    //Variables
    private String url;

    private TextView txtDetailSavedFrom;
    private TextView txtDetailSavedTo;
    private TextView txtDetailSavedDate;
    private TextView txtDetailSavedTime;
    private TextView txtDetailSavedRepeat;
    private TextView txtDetailSavedPrice;
    private ListView lstDetailSavedTravelers;

    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(RideshareApp.getAppContext());
    String token = pref.getString("accessToken", "");

    public static DetailSavedTripFragment newInstance(String id) {
        DetailSavedTripFragment fragment = new DetailSavedTripFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        url = getResources().getString(R.string.API_Trips) + getArguments().getString("id");

        getLoaderManager().initLoader(1, null, this).forceLoad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_saved_trip, container, false);

        txtDetailSavedFrom = (TextView) view.findViewById(R.id.txtDetailSavedFrom);
        txtDetailSavedTo = (TextView) view.findViewById(R.id.txtDetailSavedTo);
        txtDetailSavedDate = (TextView) view.findViewById(R.id.txtDetailSavedDate);
        txtDetailSavedTime = (TextView) view.findViewById(R.id.txtDetailSavedTime);
        txtDetailSavedRepeat = (TextView) view.findViewById(R.id.txtDetailSavedRepeat);
        txtDetailSavedPrice = (TextView) view.findViewById(R.id.txtDetailSavedPayment);
        lstDetailSavedTravelers = (ListView) view.findViewById(R.id.lstDetailSavedTravelers);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Verwijderen trip");
                builder.setMessage("Ben je zeker dat je deze trip wilt verwijderen?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Delete trip
                        APIHelper.DeleteTrip(token, getArguments().getString("id"));

                        //Get back to tripsfragment
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("Annuleer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Just dismiss the dialog
                        dialog.dismiss();
                    }
                });
                builder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Trip> onCreateLoader(int i, Bundle b) {
        return new TripLoader(getActivity(), url);
    }

    @Override
    public void onLoadFinished(Loader<Trip> Loader, Trip trip) {
        fillData(trip);
    }

    @Override
    public void onLoaderReset(Loader<Trip> Loader){}

    private void fillData(Trip trip)
    {
        txtDetailSavedFrom.setText(trip.getFrom());
        txtDetailSavedTo.setText(trip.getTo());
        txtDetailSavedDate.setText(Utils.parseISOStringToDate(trip.getDatetime()));
        txtDetailSavedTime.setText(Utils.parseISOStringToTime(trip.getDatetime()));
        txtDetailSavedRepeat.setText(Utils.parseBoolArrayToDays(trip.getRepeat()));
        txtDetailSavedPrice.setText(Utils.setPayment(trip.getPayment()));

        ArrayList<Map<String, String>> matchesList = new ArrayList<Map<String, String>>();
        for (Match m : trip.getMatches()) {
            String text = "";
            /*try {
                String token = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("accessToken", "");
                String username = Utils.getUserNameFromUserID(m.getUserID().toString(), token);
                text = username + ": from " + m.getFrom() + " to " + m.getTo() + ".";
            }
            catch (IOException e) {
                text = "unkown user" + ": from " + m.getFrom() + " to " + m.getTo() + ".";
            }*/
            text = "Unkown user" + ": from " + m.getFrom() + " to " + m.getTo() + ".";
            String status = "Status : " + Utils.convertStatus(m.getStatus());

            HashMap<String, String> item = new HashMap<String, String>();
            item.put("text", text);
            item.put("status", status);
            matchesList.add(item);
        }

        String[] from = { "text", "status" };
        int[] to = { android.R.id.text1, android.R.id.text2 };
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), matchesList,
                android.R.layout.simple_list_item_2, from, to);
        lstDetailSavedTravelers.setAdapter(adapter);
    }

}