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
import android.widget.ProgressBar;
import android.widget.TextView;

import nmct.howest.be.rideshare.Activities.Helpers.Utils;
import nmct.howest.be.rideshare.Activities.Loaders.Json.TripLoader;
import nmct.howest.be.rideshare.Activities.Models.Match;
import nmct.howest.be.rideshare.Activities.Models.Trip;
import nmct.howest.be.rideshare.R;

public class DetailRequestTripFragment extends Fragment implements LoaderManager.LoaderCallbacks<Trip> {

    private String url;
    private ImageView imgDetailRequest;
    private TextView txtDetailRequestFrom;
    private TextView txtDetailRequestTo;
    private TextView txtDetailRequestDate;
    private TextView txtDetailRequestTime;
    private TextView txtDetailRequestPrice;
    private ProgressBar progStatus;
    private ListView lstDetailRequestMessages;


    public static DetailRequestTripFragment newInstance(String id, String matchID) {
        DetailRequestTripFragment fragment = new DetailRequestTripFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("matchID", matchID);
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
        View view =  inflater.inflate(R.layout.fragment_detail_request, container, false);


        imgDetailRequest = (ImageView) view.findViewById(R.id.imgDetailVerzoek);
        txtDetailRequestFrom = (TextView) view.findViewById(R.id.txtDetailRequestFrom);
        txtDetailRequestTo = (TextView) view.findViewById(R.id.txtDetailRequestTo);
        txtDetailRequestDate = (TextView) view.findViewById(R.id.txtDetailRequestDate);
        txtDetailRequestTime = (TextView) view.findViewById(R.id.txtDetailRequestTime);
        txtDetailRequestPrice = (TextView) view.findViewById(R.id.txtDetailRequestPayment);
        progStatus = (ProgressBar) view.findViewById(R.id.progDetailRequestStatus);
        //lstDetailRequestMessages = (ListView) view.findViewById(R.id.lstDetailRequestMessages);

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
    public void onLoaderReset(Loader<Trip> loader)
    {
    }

    private void fillData(Trip trip)
    {
        txtDetailRequestFrom.setText(trip.getFrom());
        txtDetailRequestTo.setText(trip.getTo());
        txtDetailRequestDate.setText(Utils.parseISOStringToDate(trip.getDatetime()));
        txtDetailRequestTime.setText(Utils.parseISOStringToTime(trip.getDatetime()));
        txtDetailRequestPrice.setText(Utils.setPayment(trip.getPayment()));

        for(Match match : trip.getMatches()) {
            if(match.getId().equals(getArguments().getString("matchID"))) {
                progStatus.setProgress(Utils.convertStatusToProgress(match.getStatus()));
            }
        }
    }
}
