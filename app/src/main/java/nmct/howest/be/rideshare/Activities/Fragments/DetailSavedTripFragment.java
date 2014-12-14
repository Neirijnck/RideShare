package nmct.howest.be.rideshare.Activities.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nmct.howest.be.rideshare.Activities.Helpers.Utils;
import nmct.howest.be.rideshare.Activities.Loaders.Json.TripLoader;
import nmct.howest.be.rideshare.Activities.Models.Trip;
import nmct.howest.be.rideshare.R;

public class DetailSavedTripFragment extends Fragment implements LoaderManager.LoaderCallbacks<Trip> {

    private String url;

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
        url = getResources().getString(R.string.API_Trips) + getArguments().getString("id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getLoaderManager().initLoader(1, null, this).forceLoad();
        
        return inflater.inflate(R.layout.fragment_detail_saved_trip, container, false);
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
    public void onLoaderReset(Loader<Trip> Loader){

    }

    private void fillData(Trip trip)
    {
        TextView txtDetailSavedFrom = (TextView) getView().findViewById(R.id.txtDetailSavedFrom);
        TextView txtDetailSavedTo = (TextView) getView().findViewById(R.id.txtDetailSavedTo);
        TextView txtDetailSavedDate = (TextView) getView().findViewById(R.id.txtDetailSavedDate);
        TextView txtDetailSavedTime = (TextView) getView().findViewById(R.id.txtDetailSavedTime);
        TextView txtDetailSavedRepeat = (TextView) getView().findViewById(R.id.txtDetailSavedRepeat);
        TextView txtDetailSavedPrice = (TextView) getView().findViewById(R.id.txtDetailSavedPayment);
        //ListView lstDetailSavedTravelers = (ListView) getView().findViewById(R.id.lstDetailSavedTravelers);

        txtDetailSavedFrom.setText(trip.getFrom());
        txtDetailSavedTo.setText(trip.getTo());
        txtDetailSavedDate.setText(Utils.parseISOStringToDate(trip.getDatetime()));
        txtDetailSavedTime.setText(Utils.parseISOStringToTime(trip.getDatetime()));
        txtDetailSavedRepeat.setText(Utils.parseBoolArrayToDays(trip.getRepeat()));
        txtDetailSavedPrice.setText(Utils.setPayment(trip.getPayment()));

        //trip.getMatches();

    }
}
