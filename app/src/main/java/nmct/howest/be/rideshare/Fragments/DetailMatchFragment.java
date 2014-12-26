package nmct.howest.be.rideshare.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import nmct.howest.be.rideshare.Activities.OtherProfileActivity;
import nmct.howest.be.rideshare.Helpers.Utils;
import nmct.howest.be.rideshare.Loaders.Json.ProfileLoader;
import nmct.howest.be.rideshare.Loaders.Json.TripLoader;
import nmct.howest.be.rideshare.Models.Trip;
import nmct.howest.be.rideshare.Models.User;
import nmct.howest.be.rideshare.R;


public class DetailMatchFragment extends Fragment {

    private String Userid;

    private String urlTrip;
    private String urlUser;

    private static final int USER_LOADER_ID = 2;
    private static final int TRIP_LOADER_ID = 1;

    private TextView txtDetailMatchFrom;
    private TextView txtDetailMatchTo;
    private TextView txtDetailMatchDate;
    private TextView txtDetailMatchTime;

    //ProfilePictureView imgDetailMatch;
    private ImageView imgDetailMatch;
    private TextView txtDetailMatchName;
    private TextView txtDetailMatchBericht;

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

        getLoaderManager().initLoader(TRIP_LOADER_ID, null, TripLoaderListener).forceLoad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_detail_match, container, false);

        txtDetailMatchFrom = (TextView) view.findViewById(R.id.txtMatchFrom);
        txtDetailMatchTo = (TextView) view.findViewById(R.id.txtMatchTo);
        txtDetailMatchDate = (TextView) view.findViewById(R.id.txtMatchDate);
        txtDetailMatchTime = (TextView) view.findViewById(R.id.txtMatchHour);

        //imgDetailMatch = (ProfilePictureView) view.findViewById(R.id.imgDetailMatch);
        imgDetailMatch = (ImageView) view.findViewById(R.id.imgDetailMatch);
        txtDetailMatchName = (TextView) view.findViewById(R.id.txtMatchName);
        txtDetailMatchBericht = (TextView) view.findViewById(R.id.txbMatchMessage);

        return view;
    }

    private LoaderManager.LoaderCallbacks<Trip> TripLoaderListener = new LoaderManager.LoaderCallbacks<Trip>() {
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

    private void fillData(Trip trip)
    {
        txtDetailMatchFrom.setText(trip.getFrom());
        txtDetailMatchTo.setText(trip.getTo());
        txtDetailMatchDate.setText(Utils.parseISOStringToDate(trip.getDatetime()));
        txtDetailMatchTime.setText(Utils.parseISOStringToTime(trip.getDatetime()));
    }


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

    private void LoadUser(User user)
    {
        //imgDetailMatch.setProfileId(user.getFacebookID());
        imgDetailMatch.setImageBitmap(user.getBitmapFb());
        txtDetailMatchName.setText(user.getFirstName() + " " + user.getLastName());
        txtDetailMatchBericht.setText("Stuur "+user.getFirstName() + " een bericht:");
        final User mUser = user;
        imgDetailMatch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OtherProfileActivity.class);
                intent.putExtra("userID", mUser.getID() );
                getActivity().startActivity(intent);
            }
        });
    }
}
