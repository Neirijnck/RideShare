package nmct.howest.be.rideshare.Activities.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

import nmct.howest.be.rideshare.Activities.Helpers.Utils;
import nmct.howest.be.rideshare.Activities.Loaders.Json.TripLoader;
import nmct.howest.be.rideshare.Activities.Models.Trip;
import nmct.howest.be.rideshare.R;


public class DetailMatchFragment extends Fragment implements LoaderManager.LoaderCallbacks<Trip> {

    private String url;

    public static DetailMatchFragment newInstance(String id) {
        DetailMatchFragment fragment = new DetailMatchFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getResources().getString(R.string.API_Trips) + getArguments().getString("id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getLoaderManager().initLoader(1, null, this).forceLoad();

        return  inflater.inflate(R.layout.fragment_detail_match, container, false);
    }

    @Override
    public Loader<Trip> onCreateLoader(int id, Bundle args) {
        return new TripLoader(getActivity(), url);
    }

    @Override
    public void onLoadFinished(Loader<Trip> loader, Trip trip) {
        fillData(trip);
    }

    @Override
    public void onLoaderReset(Loader<Trip> loader) {

    }

    private void fillData(Trip trip)
    {
        ProfilePictureView imgDetailMatch = (ProfilePictureView) getView().findViewById(R.id.imgDetailMatch);
        TextView txtDetailMatchFrom = (TextView) getView().findViewById(R.id.txbMatchVan);
        TextView txtDetailMatchName = (TextView) getView().findViewById(R.id.txbMatchNaam);
        TextView txtDetailMatchTo = (TextView) getView().findViewById(R.id.txbMatchNaar);
        TextView txtDetailMatchDate = (TextView) getView().findViewById(R.id.txbMatchDag);
        TextView txtDetailMatchTime = (TextView) getView().findViewById(R.id.txbMatchUur);

        //ListView lstDetailMatchMessages = (ListView) getView().findViewById(R.id.lstDetailMatchMessages);


        imgDetailMatch.setProfileId(trip.getUserID());
        Log.d("user", ""+ trip.getUserID());
        txtDetailMatchFrom.setText("Van: "+ trip.getFrom());
        txtDetailMatchTo.setText("Naar: "+trip.getTo());
        txtDetailMatchDate.setText("Dag: "+Utils.parseISOStringToDate(trip.getDatetime()));
        txtDetailMatchTime.setText("Uur: "+Utils.parseISOStringToTime(trip.getDatetime()));


    }
}
