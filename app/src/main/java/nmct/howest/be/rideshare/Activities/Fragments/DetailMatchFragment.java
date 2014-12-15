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
import nmct.howest.be.rideshare.Activities.Loaders.Json.ProfileLoader;
import nmct.howest.be.rideshare.Activities.Loaders.Json.TripLoader;
import nmct.howest.be.rideshare.Activities.Models.Trip;
import nmct.howest.be.rideshare.Activities.Models.User;
import nmct.howest.be.rideshare.R;


public class DetailMatchFragment extends Fragment {

    private String urlTrip;
    private String urlUser;
    String Userid;
    private static final int USER_LOADER_ID = 2;
    private static final int TRIP_LOADER_ID = 1;

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
        urlTrip = getResources().getString(R.string.API_Trips) + getArguments().getString("id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getLoaderManager().initLoader(TRIP_LOADER_ID, null, TripLoaderListener).forceLoad();

        return  inflater.inflate(R.layout.fragment_detail_match, container, false);
    }

    private LoaderManager.LoaderCallbacks<Trip> TripLoaderListener
            = new LoaderManager.LoaderCallbacks<Trip>() {
        @Override
        public Loader<Trip> onCreateLoader(int id, Bundle args) {
            return new TripLoader(getActivity(), urlTrip);
        }


        @Override
        public void onLoadFinished(Loader<Trip> loader, Trip trip) {
            Userid = trip.getUserID();
            urlUser = getResources().getString(R.string.API_Profile) + trip.getUserID();
            getLoaderManager().initLoader(USER_LOADER_ID, null, UserLoaderListener).forceLoad();
            fillData(trip);
        }

        @Override
        public void onLoaderReset(Loader<Trip> loader) {

        }
    };
    private LoaderManager.LoaderCallbacks<User> UserLoaderListener
            = new LoaderManager.LoaderCallbacks<User>() {

        @Override
        public Loader<User> onCreateLoader(int id, Bundle args) {
            return new ProfileLoader(getActivity(), urlUser);
        }


        @Override
        public void onLoadFinished(Loader<User> loader, User user) {
                LoadUser(user);
        }

        @Override
        public void onLoaderReset(Loader<User> loader) {

        }
    };
    private void fillData(Trip trip)
    {

        TextView txtDetailMatchFrom = (TextView) getView().findViewById(R.id.txbMatchVan);
        TextView txtDetailMatchTo = (TextView) getView().findViewById(R.id.txbMatchNaar);
        TextView txtDetailMatchDate = (TextView) getView().findViewById(R.id.txbMatchDag);
        TextView txtDetailMatchTime = (TextView) getView().findViewById(R.id.txbMatchUur);

        //ListView lstDetailMatchMessages = (ListView) getView().findViewById(R.id.lstDetailMatchMessages);

        txtDetailMatchFrom.setText("Van: "+ trip.getFrom());
        txtDetailMatchTo.setText("Naar: "+trip.getTo());
        txtDetailMatchDate.setText("Dag: "+Utils.parseISOStringToDate(trip.getDatetime()));
        txtDetailMatchTime.setText("Uur: "+Utils.parseISOStringToTime(trip.getDatetime()));


    }
    private void LoadUser(User user)
    {
        Log.d("user", ""+ user.getFirstName());
        ProfilePictureView imgDetailMatch = (ProfilePictureView) getView().findViewById(R.id.imgDetailMatch);
        TextView txtDetailMatchName = (TextView) getView().findViewById(R.id.txbMatchNaam);
        TextView txtDetailMatchBericht = (TextView) getView().findViewById(R.id.txbMatchBericht);
        imgDetailMatch.setProfileId(user.getFacebookID());
        txtDetailMatchName.setText(user.getFirstName() + " " + user.getLastName());
        txtDetailMatchBericht.setText("Stuur "+user.getFirstName() + " een bericht:");
    }
}
