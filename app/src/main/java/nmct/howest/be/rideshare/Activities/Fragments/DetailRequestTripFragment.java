package nmct.howest.be.rideshare.Activities.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import nmct.howest.be.rideshare.Activities.Helpers.Utils;
import nmct.howest.be.rideshare.Activities.Loaders.Json.TripLoader;
import nmct.howest.be.rideshare.Activities.Models.Trip;
import nmct.howest.be.rideshare.R;

public class DetailRequestTripFragment extends Fragment implements LoaderManager.LoaderCallbacks<Trip> {

    private String url;

    public static DetailRequestTripFragment newInstance(String id) {
        DetailRequestTripFragment fragment = new DetailRequestTripFragment();
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

        return inflater.inflate(R.layout.fragment_detail_request, container, false);
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
        ImageView imgDetailRequest = (ImageView) getView().findViewById(R.id.imgDetailRequest);
        TextView txtDetailRequestFrom = (TextView) getView().findViewById(R.id.txtDetailRequestFrom);
        TextView txtDetailRequestTo = (TextView) getView().findViewById(R.id.txtDetailRequestTo);
        TextView txtDetailRequestDate = (TextView) getView().findViewById(R.id.txtDetailRequestDate);
        TextView txtDetailRequestTime = (TextView) getView().findViewById(R.id.txtDetailRequestTime);
        TextView txtDetailRequestPrice = (TextView) getView().findViewById(R.id.txtDetailRequestPayment);
        //ListView lstDetailRequestMessages = (ListView) getView().findViewById(R.id.lstDetailRequestMessages);

        txtDetailRequestFrom.setText(trip.getFrom());
        txtDetailRequestTo.setText(trip.getTo());
        txtDetailRequestDate.setText(Utils.parseISOStringToDate(trip.getDatetime()));
        txtDetailRequestTime.setText(Utils.parseISOStringToTime(trip.getDatetime()));
        txtDetailRequestPrice.setText(Utils.setPayment(trip.getPayment()));
    }
}
