package nmct.howest.be.rideshare.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nmct.howest.be.rideshare.Activities.OtherProfileActivity;
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
import nmct.howest.be.rideshare.RideshareApp;

public class DetailRequestTripFragment extends Fragment {

    //Variables
    private ArrayAdapter<Message> mAdapterMessages;

    private String urlTrip;
    private String urlUser;

    private static final int USER_LOADER_ID = 2;
    private static final int TRIP_LOADER_ID = 1;

    private TextView txtDetailRequestFrom;
    private TextView txtDetailRequestTo;
    private TextView txtDetailRequestDate;
    private TextView txtDetailRequestTime;
    private TextView txtDetailRequestPrice;
    private ProgressBar progStatus;

    private ListView lstDetailRequestMessages;

    private ImageView imgDetailRequest;
    private TextView txbDetailRequestName;
    private EditText txtDetailRequestAddMessage;
    private Button btnDetailRequestAddMessage;

    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(RideshareApp.getAppContext());
    String token = pref.getString("accessToken", "");

    public static DetailRequestTripFragment newInstance(String Id, String matchId) {
        DetailRequestTripFragment fragment = new DetailRequestTripFragment();
        Bundle args = new Bundle();
        args.putString("id", Id);
        args.putString("matchID", matchId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Verwijderen aanvraag");
                builder.setMessage("Ben je zeker dat je deze aanvraag wilt verwijderen?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Delete trip request
                        APIHelper.DeleteRequest(token, getArguments().getString("id"), getArguments().getString("matchID"));

                        //Get back to tripsfragment
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("Annuleer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Just dismiss the dialog
                        dialog.dismiss();
                    }
                });
                builder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        urlTrip = getResources().getString(R.string.API_Trips) + getArguments().getString("id");

        getLoaderManager().initLoader(TRIP_LOADER_ID, null, TripLoaderListener).forceLoad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_detail_request, container, false);

        imgDetailRequest = (ImageView) view.findViewById(R.id.imgDetailRequest);
        txbDetailRequestName = (TextView) view.findViewById(R.id.txbDetailRequestName);

        txtDetailRequestFrom = (TextView) view.findViewById(R.id.txtDetailRequestFrom);
        txtDetailRequestTo = (TextView) view.findViewById(R.id.txtDetailRequestTo);
        txtDetailRequestDate = (TextView) view.findViewById(R.id.txtDetailRequestDate);
        txtDetailRequestTime = (TextView) view.findViewById(R.id.txtDetailRequestTime);
        txtDetailRequestPrice = (TextView) view.findViewById(R.id.txtDetailRequestPayment);
        progStatus = (ProgressBar) view.findViewById(R.id.progDetailRequestStatus);

        lstDetailRequestMessages = (ListView) view.findViewById(R.id.lstDetailRequestMessages);
        mAdapterMessages = new MessagesAdapter(getActivity(), R.layout.item_message, R.id.txbMessageDate);
        lstDetailRequestMessages.setAdapter(mAdapterMessages);

        txtDetailRequestAddMessage = (EditText) view.findViewById(R.id.txtDetailRequestAddMessage);
        btnDetailRequestAddMessage = (Button) view.findViewById(R.id.btnDetailRequestAddMessage);

        txtDetailRequestAddMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

        btnDetailRequestAddMessage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        return view;
    }

    public void sendMessage() {
        Log.d("send ", txtDetailRequestAddMessage.getText().toString());
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
        public void onLoaderReset(Loader<Trip> loader) { }
    };

    private void fillData(Trip trip)
    {
        txtDetailRequestFrom.setText(trip.getFrom());
        txtDetailRequestTo.setText(trip.getTo());
        txtDetailRequestDate.setText(Utils.parseISOStringToDate(trip.getDatetime()));
        txtDetailRequestTime.setText(Utils.parseISOStringToTime(trip.getDatetime()));
        txtDetailRequestPrice.setText(Utils.setPayment(trip.getPayment()));

        for(Match match : trip.getMatches()) {
            if(match.getId().equals(getArguments().getString("matchID"))) {

                //messages
                List<Message> lm = new ArrayList<Message>();

                Message m = new Message();
                m.setDatetime(match.getDatetime());
                m.setText("test");
                m.setUserID(match.getUserID());
                lm.add(m);

                Message n = new Message();
                n.setDatetime(match.getDatetime());
                n.setText("testje");
                n.setUserID(match.getUserID());
                lm.add(n);

                match.setMessages(lm);

                mAdapterMessages.addAll(match.getMessages());

                progStatus.setProgress(Utils.convertStatusToProgress(match.getStatus()));
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
        final String userID = user.getID();
        imgDetailRequest.setImageBitmap(user.getBitmapFb());
        imgDetailRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OtherProfileActivity.class);
                intent.putExtra("userID", userID);
                getActivity().startActivity(intent);
            }
        });
        txbDetailRequestName.setText(user.getFirstName() + " " + user.getLastName());
    }

}
