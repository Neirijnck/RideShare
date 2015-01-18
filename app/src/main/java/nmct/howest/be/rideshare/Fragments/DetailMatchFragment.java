package nmct.howest.be.rideshare.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nmct.howest.be.rideshare.Activities.MainActivity;
import nmct.howest.be.rideshare.Activities.OtherProfileActivity;
import nmct.howest.be.rideshare.Helpers.APIHelper;
import nmct.howest.be.rideshare.Helpers.Utils;
import nmct.howest.be.rideshare.Loaders.Json.ProfileLoader;
import nmct.howest.be.rideshare.Loaders.Json.TripLoader;
import nmct.howest.be.rideshare.Models.Match;
import nmct.howest.be.rideshare.Models.Message;
import nmct.howest.be.rideshare.Models.Trip;
import nmct.howest.be.rideshare.Models.User;
import nmct.howest.be.rideshare.R;
import nmct.howest.be.rideshare.RideshareApp;


public class DetailMatchFragment extends Fragment {

    //Variables
    private Trip mTrip;

    private String urlTrip;
    private String urlUser;

    private static final int USER_LOADER_ID = 2;
    private static final int TRIP_LOADER_ID = 1;

    private TextView txtDetailMatchFrom;
    private TextView txtDetailMatchTo;
    private TextView txtDetailMatchDate;
    private TextView txtDetailMatchTime;
    private ImageView imgDetailMatch;
    private TextView txtDetailMatchName;
    private TextView txtDetailMatchBericht;
    private EditText txtMessage;
    private Button btnJoinRide;
    private ProgressBar mProgressMatch;
    private ScrollView mLayoutMatch;

    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(RideshareApp.getAppContext());
    String token = pref.getString("accessToken", "");

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

        mProgressMatch = (ProgressBar) view.findViewById(R.id.progressBarMatch);
        mLayoutMatch = (ScrollView) view.findViewById(R.id.container_match);

        txtDetailMatchFrom = (TextView) view.findViewById(R.id.txtMatchFrom);
        txtDetailMatchTo = (TextView) view.findViewById(R.id.txtMatchTo);
        txtDetailMatchDate = (TextView) view.findViewById(R.id.txtMatchDate);
        txtDetailMatchTime = (TextView) view.findViewById(R.id.txtMatchHour);

        imgDetailMatch = (ImageView) view.findViewById(R.id.imgDetailMatch);
        txtDetailMatchName = (TextView) view.findViewById(R.id.txtMatchName);
        txtDetailMatchBericht = (TextView) view.findViewById(R.id.txbMatchMessage);
        txtMessage = (EditText) view.findViewById(R.id.txtMatchAdded);

        btnJoinRide = (Button) view.findViewById(R.id.btnMatchAccept);

        return view;
    }

    private LoaderManager.LoaderCallbacks<Trip> TripLoaderListener = new LoaderManager.LoaderCallbacks<Trip>() {
        @Override
        public Loader<Trip> onCreateLoader(int id, Bundle args) {
            return new TripLoader(getActivity(), urlTrip);
        }


        @Override
        public void onLoadFinished(Loader<Trip> loader, Trip trip) {
            urlUser = getResources().getString(R.string.API_Profile) + trip.getUserID();
            getLoaderManager().initLoader(USER_LOADER_ID, null, UserLoaderListener).forceLoad();
            mTrip = trip;
            fillData(mTrip);
        }

        @Override
        public void onLoaderReset(Loader<Trip> loader) {}
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
        imgDetailMatch.setImageBitmap(user.getBitmapFb());
        txtDetailMatchName.setText(user.getFirstName() + " " + user.getLastName());
        txtDetailMatchBericht.setText("Stuur "+user.getFirstName() + " een bericht:");

        //Make layout visible when loaded and remove progressbar
        mProgressMatch.setVisibility(View.INVISIBLE);
        mLayoutMatch.setVisibility(View.VISIBLE);

        final User mUser = user;
        imgDetailMatch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OtherProfileActivity.class);
                intent.putExtra("userID", mUser.getID() );
                getActivity().startActivity(intent);
            }
        });

        //Add this match
        btnJoinRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //New match
                Match match = new Match();
                match.setFrom(mTrip.getFrom());
                match.setTo(mTrip.getTo());
                //With a message
                List<Message> messages = new ArrayList<Message>();
                if(!TextUtils.isEmpty(txtMessage.getText().toString()))
                {
                    Message message = new Message();
                    message.setText(txtMessage.getText().toString().trim());
                    message.setDatetime(Utils.parseNowToISOString());
                    messages.add(message);
                }
                match.setMessages(messages);
                APIHelper.AddMatch(token, match, mTrip.getID());
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

}
