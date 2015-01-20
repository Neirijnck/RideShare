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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class DetailRequestTripFragment extends Fragment {

    //Variables
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
    private ProgressBar progressRequest;
    private ScrollView mLayoutRequest;

    private LinearLayout lstDetailRequestMessages;

    private ImageView imgDetailRequest;
    private TextView txbDetailRequestName;
    private EditText txtDetailRequestAddMessage;
    private Button btnDetailRequestAddMessage;

    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(RideshareApp.getAppContext());
    String token = pref.getString("accessToken", "");
    String myUserID = pref.getString("myUserID", "");

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
                builder.setTitle(getActivity().getResources().getString(R.string.dialogTitleDeleteRequest));
                builder.setMessage(getActivity().getResources().getString(R.string.dialogDeleteRequest));
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Delete trip request
                        APIHelper.DeleteRequest(token, getArguments().getString("id"), getArguments().getString("matchID"));

                        //Get back to tripsfragment
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton(getActivity().getResources().getString(R.string.dialogCancel), new DialogInterface.OnClickListener() {
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
        progressRequest = (ProgressBar) view.findViewById(R.id.progressBarRequest);
        mLayoutRequest = (ScrollView) view.findViewById(R.id.container_request);

        lstDetailRequestMessages = (LinearLayout) view.findViewById(R.id.lstDetailRequestMessages);
        txtDetailRequestAddMessage = (EditText) view.findViewById(R.id.txtDetailRequestAddMessage);
        btnDetailRequestAddMessage = (Button) view.findViewById(R.id.btnDetailRequestAddMessage);

        txtDetailRequestAddMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    Utils.sendMessage(token, txtDetailRequestAddMessage, getArguments().getString("matchID"), getArguments().getString("id"));
                    getLoaderManager().restartLoader(TRIP_LOADER_ID, null, TripLoaderListener).forceLoad();
                    handled = true;
                }
                return handled;
            }
        });

        btnDetailRequestAddMessage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.sendMessage(token, txtDetailRequestAddMessage, getArguments().getString("matchID"), getArguments().getString("id"));
                getLoaderManager().restartLoader(TRIP_LOADER_ID, null, TripLoaderListener).forceLoad();
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
                Utils.populateMessages(getActivity().getLayoutInflater(), lstDetailRequestMessages , match.getMessages(), myUserID);
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
        public void onLoaderReset(Loader<User> loader) {}
    };

    private void LoadUser(User user)
    {
        progressRequest.setVisibility(View.INVISIBLE);
        mLayoutRequest.setVisibility(View.VISIBLE);

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
