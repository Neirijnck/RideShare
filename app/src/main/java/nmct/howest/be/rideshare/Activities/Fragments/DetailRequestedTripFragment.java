package nmct.howest.be.rideshare.Activities.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import nmct.howest.be.rideshare.Activities.Helpers.Utils;
import nmct.howest.be.rideshare.Activities.Loaders.Json.TripLoader;
import nmct.howest.be.rideshare.Activities.Models.Trip;
import nmct.howest.be.rideshare.R;

public class DetailRequestedTripFragment extends Fragment implements LoaderManager.LoaderCallbacks<Trip> {

    private String url;
    private ImageView imgDetailRequested;
    private TextView txtRequestedFrom;
    private TextView txtRequestedTo;
    private TextView txtRequestedDate;
    private TextView txtRequestedHour;
    private TextView txtRequestedPayment;

    public static DetailRequestedTripFragment newInstance(String id) {
        DetailRequestedTripFragment fragment = new DetailRequestedTripFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getResources().getString(R.string.API_Trips) + getArguments().getString("id");

        getLoaderManager().initLoader(1, null, this).forceLoad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_detail_requested, container, false);

        imgDetailRequested = (ImageView) view.findViewById(R.id.imgDetailRequested);
        txtRequestedFrom = (TextView) view.findViewById(R.id.txtRequestedFrom);
        txtRequestedTo = (TextView) view.findViewById(R.id.txtRequestedTo);
        txtRequestedDate = (TextView) view.findViewById(R.id.txtRequestedDate);
        txtRequestedHour = (TextView) view.findViewById(R.id.txtRequestedHour);
        txtRequestedPayment = (TextView) view.findViewById(R.id.txtRequestedPayment);

        return view;
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
        txtRequestedFrom.setText(trip.getFrom());
        txtRequestedTo.setText(trip.getTo());
        txtRequestedDate.setText(Utils.parseISOStringToDate(trip.getDatetime()));
        txtRequestedHour.setText(Utils.parseISOStringToTime(trip.getDatetime()));
        txtRequestedPayment.setText(Utils.setPayment(trip.getPayment()));
    }
}
