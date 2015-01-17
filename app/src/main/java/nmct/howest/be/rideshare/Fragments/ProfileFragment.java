package nmct.howest.be.rideshare.Fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import nmct.howest.be.rideshare.Adapters.ReviewRecyclerAdapter;
import nmct.howest.be.rideshare.Helpers.APIHelper;
import nmct.howest.be.rideshare.Loaders.Json.ProfileLoader;
import nmct.howest.be.rideshare.Models.Review;
import nmct.howest.be.rideshare.Models.User;
import nmct.howest.be.rideshare.R;


public class ProfileFragment extends Fragment implements LoaderManager.LoaderCallbacks<User> {

    //Variables
    private User CurrentUser;
    private String UserId;
    private ProfilePictureView profilePictureView;
    private TextView txtName;
    private TextView txtPlace;
    private TextView txtGenderAge;
    private TextView txtCar;
    private TextView txtUserName;
    private List<Review> reviews = new ArrayList<Review>();

    private RecyclerView mReviewRecyclerView;
    private ReviewRecyclerAdapter mReviewRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final int USER_LOADER_ID = 1;
    private static final int CURRENT_USER_LOADER_ID = 2;

    public ProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle b = getActivity().getIntent().getExtras();
        UserId = b.getString("userID");

        //Init loader to get data
        getLoaderManager().initLoader(USER_LOADER_ID, null, this).forceLoad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_other_profile, container, false);

        mReviewRecyclerView = (RecyclerView) view.findViewById(R.id.lstProfileReviews);

        // Setting the LayoutManager.
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mReviewRecyclerView.setLayoutManager(mLayoutManager);

        // Setting the adapter.
        mReviewRecyclerAdapter = new ReviewRecyclerAdapter(reviews);
        mReviewRecyclerView.setAdapter(mReviewRecyclerAdapter);

        Button btnGo=(Button) view.findViewById(R.id.btnOtherProfileReview);
        btnGo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                itemRatingPopup();
            }
        });

        return view;
    }

    private LoaderManager.LoaderCallbacks<User> CurrentUserLoader
            = new LoaderManager.LoaderCallbacks<User>() {

        @Override
        public Loader<User> onCreateLoader(int id, Bundle args) {
            return new ProfileLoader(getActivity(), getResources().getString(R.string.API_Profile));
        }

        @Override
        public void onLoadFinished(Loader<User> loader, User user) {
            CurrentUser = user;
        }

        @Override
        public void onLoaderReset(Loader<User> loader) {}
    };

    //Implementation of ProfileLoader
    @Override
    public Loader<User> onCreateLoader(int i, Bundle bundle) {
        return new ProfileLoader(getActivity(), getResources().getString(R.string.API_Profile) + UserId );
    }

    @Override
    public void onLoadFinished(Loader<User> Loader, User user) {

        fillData(user);
        getActivity().setTitle(user.getUserName());

        if (user.getReviews() != null) {
            reviews.addAll(user.getReviews());

            //Sort list so it's always the same
            Collections.sort(reviews, Collections.reverseOrder(new Review.compareToDate()));

            mReviewRecyclerAdapter.updateList(reviews);
        }

        //If list isnt empty, show the list
        if (!reviews.isEmpty()) {
            TextView txbNoReviews = (TextView) getActivity().findViewById(R.id.txbProfileNoReviews);
            txbNoReviews.setVisibility(View.INVISIBLE);

            mReviewRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<User> Loader) {
        reviews.clear();
        mReviewRecyclerAdapter.updateList(reviews);
    }

    private void fillData(User user) {

        txtName = (TextView) getView().findViewById(R.id.txtOtherName);
        txtPlace = (TextView) getView().findViewById(R.id.txtOtherPlace);
        txtGenderAge = (TextView) getView().findViewById(R.id.txtOtherGenderAge);
        txtCar = (TextView) getView().findViewById(R.id.txtOtherCar);
        txtUserName = (TextView) getView().findViewById(R.id.txtOtherUserName);
        profilePictureView = (ProfilePictureView) getView().findViewById(R.id.imgOtherProfilePicture);

        txtName.setText(user.getFirstName() + " " + user.getLastName());

        if (TextUtils.isEmpty(user.getLocation())) {
            txtPlace.setText("Locatie niet bekend");
        } else {
            txtPlace.setText(user.getLocation());
        }
        txtUserName.setText(user.getUserName());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getDefault());
        String birthday = "";
        try {
            Date date = sdf.parse(user.getBirthday().toString());

            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
            birthday = fmtOut.format(date);
        } catch (ParseException ex) {
            Log.e("ParseException Date", ex.getMessage());
        }
        txtGenderAge.setText(user.getGender() + ", " + birthday);

        if (!TextUtils.isEmpty(user.getGender()) && !TextUtils.isEmpty(user.getBirthday())) {
            txtGenderAge.setText(user.getGender() + ", " + birthday);
        } else if (TextUtils.isEmpty(user.getGender()) && !TextUtils.isEmpty(user.getBirthday())) {
            txtGenderAge.setText(user.getBirthday());
        } else if (!TextUtils.isEmpty(user.getGender()) && TextUtils.isEmpty(user.getBirthday())) {
            txtGenderAge.setText(user.getGender());
        } else {
            txtGenderAge.setText("Verjaardag niet bekend");
        }

        if (!TextUtils.isEmpty(user.getCarType()) && !TextUtils.isEmpty(user.getAmountOfSeats())) {
            txtCar.setText(user.getCarType() + " (" + user.getAmountOfSeats() + " pl.)");
        } else if (!TextUtils.isEmpty(user.getCarType()) && TextUtils.isEmpty(user.getAmountOfSeats())) {
            txtCar.setText(user.getCarType());
        } else if (TextUtils.isEmpty(user.getCarType()) && !TextUtils.isEmpty(user.getAmountOfSeats())) {
            txtCar.setText(user.getAmountOfSeats() + " plaatsen");
        } else {
            txtCar.setText("Auto niet bekend");
        }

        if (!TextUtils.isEmpty(user.getFacebookID()))
            profilePictureView.setProfileId(user.getFacebookID());
        profilePictureView.setCropped(true);
    }

    private void itemRatingPopup()
    {
        getLoaderManager().initLoader(CURRENT_USER_LOADER_ID, null, CurrentUserLoader).forceLoad();
        final Dialog popDialog = new Dialog(getActivity());
        popDialog.setContentView(R.layout.dialog_review);
        popDialog.setTitle("Review");

        final RatingBar ratingBar = (RatingBar) popDialog.findViewById(R.id.rtbScore);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        // Button OK

        Button dialogButton = (Button) popDialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                String date = df.format(Calendar.getInstance().getTime());
                date = date + "Z";
                int rating = (int)ratingBar.getRating();

                EditText text = (EditText) popDialog.findViewById(R.id.editTextDialogUserInput);
                Review review = new Review(CurrentUser.getID(), CurrentUser.getUserName(), date, rating, text.getText().toString().trim());
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String token = pref.getString("accessToken", "");
                APIHelper.AddReview(token,review,UserId);
                Log.d("time", text.getText().toString()+ " " + rating);
                popDialog.dismiss();
            }
        });
        popDialog.show();
    }

}
