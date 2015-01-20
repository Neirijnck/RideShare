package nmct.howest.be.rideshare.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import nmct.howest.be.rideshare.Activities.OtherProfileActivity;
import nmct.howest.be.rideshare.Helpers.APIHelper;
import nmct.howest.be.rideshare.Helpers.Utils;
import nmct.howest.be.rideshare.Loaders.Json.ProfileLoader;
import nmct.howest.be.rideshare.Loaders.Json.TripLoader;
import nmct.howest.be.rideshare.Models.Match;
import nmct.howest.be.rideshare.Models.Trip;
import nmct.howest.be.rideshare.Models.User;
import nmct.howest.be.rideshare.R;
import nmct.howest.be.rideshare.RideshareApp;

public class DetailRequestedTripFragment extends Fragment {

    //Variables
    private String urlTrip;
    private String urlUser;

    private static final int TRIP_LOADER_ID = 1;
    private static final int USER_LOADER_ID = 2;

    private TextView txtRequestedFrom;
    private TextView txtRequestedTo;
    private TextView txtRequestedDate;
    private TextView txtRequestedTime;
    private TextView txtRequestedPrice;
    private ProgressBar mProgressRequested;
    private ScrollView mLayoutRequested;

    private LinearLayout lstDetailRequestedMessages;

    private ImageView imgDetailRequested;
    private TextView txbDetailRequestedName;
    private EditText txtDetailRequestedAddMessage;
    private Button btnDetailRequestedAddMessage;

    private Button btnDetailRequestedAccept;
    private Button btnDetailRequestedDecline;

    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(RideshareApp.getAppContext());
    String token = pref.getString("accessToken", "");
    String myUserID = pref.getString("myUserID", "");

    public static DetailRequestedTripFragment newInstance(String Id, String matchId) {
        DetailRequestedTripFragment fragment = new DetailRequestedTripFragment();
        Bundle args = new Bundle();
        args.putString("id", Id);
        args.putString("matchID", matchId);
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
        View view =  inflater.inflate(R.layout.fragment_detail_requested, container, false);

        imgDetailRequested = (ImageView) view.findViewById(R.id.imgDetailRequested);
        txbDetailRequestedName = (TextView) view.findViewById(R.id.txbDetailRequestedName);
        txtRequestedFrom = (TextView) view.findViewById(R.id.txtDetailRequestedFrom);
        txtRequestedTo = (TextView) view.findViewById(R.id.txtDetailRequestedTo);
        txtRequestedDate = (TextView) view.findViewById(R.id.txtDetailRequestedDate);
        txtRequestedTime = (TextView) view.findViewById(R.id.txtDetailRequestedTime);
        txtRequestedPrice = (TextView) view.findViewById(R.id.txtDetailRequestedPrice);
        mProgressRequested = (ProgressBar) view.findViewById(R.id.progressBarRequested);
        mLayoutRequested = (ScrollView) view.findViewById(R.id.container_requested);

        lstDetailRequestedMessages = (LinearLayout) view.findViewById(R.id.lstDetailRequestedMessages);
        txtDetailRequestedAddMessage = (EditText) view.findViewById(R.id.txtDetailRequestedAddMessage);
        btnDetailRequestedAddMessage = (Button) view.findViewById(R.id.btnDetailRequestedAddMessage);

        btnDetailRequestedAccept = (Button) view.findViewById(R.id.btnDetailRequestedAccept);
        btnDetailRequestedDecline = (Button) view.findViewById(R.id.btnDetailRequestedDecline);

        txtDetailRequestedAddMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    Utils.sendMessage(token, txtDetailRequestedAddMessage, getArguments().getString("matchID"), getArguments().getString("id"));
                    getLoaderManager().restartLoader(TRIP_LOADER_ID, null, TripLoaderListener).forceLoad();
                    txtDetailRequestedAddMessage.setText("");
                    handled = true;
                }
                return handled;
            }
        });

        btnDetailRequestedAddMessage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.sendMessage(token, txtDetailRequestedAddMessage, getArguments().getString("matchID"), getArguments().getString("id"));
                getLoaderManager().restartLoader(TRIP_LOADER_ID, null, TripLoaderListener).forceLoad();
                txtDetailRequestedAddMessage.setText("");
            }
        });

        btnDetailRequestedAccept.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //status 1 - Accepted
                APIHelper.UpdateMatch(token, getArguments().getString("matchID"), 1, getArguments().getString("id"));
                getActivity().finish();
            }
        });

        btnDetailRequestedDecline.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //status 2 - Declined
                APIHelper.UpdateMatch(token, getArguments().getString("matchID"), 2, getArguments().getString("id"));
                getActivity().finish();
            }
        });

        return view;
    }

    private LoaderManager.LoaderCallbacks<Trip> TripLoaderListener = new LoaderManager.LoaderCallbacks<Trip>() {
        @Override
        public Loader<Trip> onCreateLoader(int id, Bundle args) {
            return new TripLoader(getActivity(), urlTrip);
        }

        @Override
        public void onLoadFinished(Loader<Trip> loader, Trip trip) {
            for(Match match : trip.getMatches())
            {
                if(match.getId().equals(getArguments().getString("matchID")))
                {
                    urlUser = getResources().getString(R.string.API_Profile) + match.getUserID();
                    getLoaderManager().initLoader(USER_LOADER_ID, null, UserLoaderListener).forceLoad();
                    fillData(trip);
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Trip> loader) {}
    };

    private void fillData(Trip trip)
    {
        txtRequestedFrom.setText(trip.getFrom());
        txtRequestedTo.setText(trip.getTo());
        txtRequestedDate.setText(Utils.parseISOStringToDate(trip.getDatetime()));
        txtRequestedTime.setText(Utils.parseISOStringToTime(trip.getDatetime()));
        txtRequestedPrice.setText(Utils.setPayment(trip.getPayment()));

        for(Match match : trip.getMatches()) {
            if(match.getId().equals(getArguments().getString("matchID"))) {
                Utils.populateMessages(getActivity().getLayoutInflater(), lstDetailRequestedMessages, match.getMessages(), myUserID);
            }
        }
    }

    private LoaderManager.LoaderCallbacks<User> UserLoaderListener= new LoaderManager.LoaderCallbacks<User>() {

        @Override
        public Loader<User> onCreateLoader(int id, Bundle args) {
            return new ProfileLoader(getActivity(), urlUser);
        }

        @Override
        public void onLoadFinished(Loader<User> loader, User user) {
            LoadUser(user);
        }

        @Override
        public void onLoaderReset(Loader<User> loader) {}
    };

    private void LoadUser(User user)
    {
        mProgressRequested.setVisibility(View.INVISIBLE);
        mLayoutRequested.setVisibility(View.VISIBLE);

        final String userID = user.getID();
        imgDetailRequested.setImageBitmap(user.getBitmapFb());
        imgDetailRequested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OtherProfileActivity.class);
                intent.putExtra("userID", userID);
                getActivity().startActivity(intent);
            }
        });
        txbDetailRequestedName.setText(user.getFirstName() + " " + user.getLastName());
    }

}
