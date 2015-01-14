package nmct.howest.be.rideshare.Fragments;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import nmct.howest.be.rideshare.Adapters.MessagesAdapter;
import nmct.howest.be.rideshare.Helpers.APIHelper;
import nmct.howest.be.rideshare.Helpers.Utils;
import nmct.howest.be.rideshare.Loaders.Json.ProfileLoader;
import nmct.howest.be.rideshare.Loaders.Json.TripLoader;
import nmct.howest.be.rideshare.Models.Match;
import nmct.howest.be.rideshare.Models.Message;
import nmct.howest.be.rideshare.Models.Trip;
import nmct.howest.be.rideshare.Models.User;
import nmct.howest.be.rideshare.R;

public class DetailRequestedTripFragment extends Fragment {

    private ArrayAdapter<Message> mAdapterMessages;

    private String urlTrip;
    private String urlUser;

    private static final int TRIP_LOADER_ID = 1;
    private static final int USER_LOADER_ID = 2;

    private TextView txtRequestedFrom;
    private TextView txtRequestedTo;
    private TextView txtRequestedDate;
    private TextView txtRequestedTime;
    private TextView txtRequestedPrice;

    private ListView lstDetailRequestedMessages;

    private ImageView imgDetailRequested;
    private TextView txbDetailRequestedName;
    private EditText txtDetailRequestedAddMessage;
    private Button btnDetailRequestedAddMessage;

    private Button btnDetailRequestedAccept;
    private Button btnDetailRequestedDecline;

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

        lstDetailRequestedMessages = (ListView) view.findViewById(R.id.lstDetailRequestedMessages);
        mAdapterMessages = new MessagesAdapter(getActivity(), R.layout.item_message, R.id.txbMessageDate);
        lstDetailRequestedMessages.setAdapter(mAdapterMessages);

        txtDetailRequestedAddMessage = (EditText) view.findViewById(R.id.txtDetailRequestedAddMessage);
        btnDetailRequestedAddMessage = (Button) view.findViewById(R.id.btnDetailRequestedAddMessage);

        btnDetailRequestedAccept = (Button) view.findViewById(R.id.btnDetailRequestedAccept);
        btnDetailRequestedDecline = (Button) view.findViewById(R.id.btnDetailRequestedDecline);

        txtDetailRequestedAddMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendMessage();
                    handled = true;
                }
                return handled;
            }
        });

        btnDetailRequestedAddMessage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        btnDetailRequestedAccept.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //status 1
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String token = pref.getString("accessToken", "");
                APIHelper.UpdateMatch(token, getArguments().getString("matchID"), 1, getArguments().getString("id"));
            }
        });

        btnDetailRequestedDecline.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //status 2
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String token = pref.getString("accessToken", "");
                APIHelper.UpdateMatch(token, getArguments().getString("matchID"), 2, getArguments().getString("id"));
            }
        });

        return view;
    }

    public void sendMessage() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String token = pref.getString("accessToken", "");

        Message m = new Message();
        m.setText(txtDetailRequestedAddMessage.getText().toString());
        m.setDatetime(Utils.parseNowToISOString());

        APIHelper.AddMessageToMatch(token, getArguments().getString("matchID"), m, getArguments().getString("id"));
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
            fillData(trip);
        }

        @Override
        public void onLoaderReset(Loader<Trip> loader) {

        }
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
                mAdapterMessages.addAll(match.getMessages());
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
        public void onLoaderReset(Loader<User> loader) {

        }
    };

    private void LoadUser(User user)
    {
        imgDetailRequested.setImageBitmap(user.getBitmapFb());
        txbDetailRequestedName.setText(user.getFirstName() + " " + user.getLastName());
    }
}
