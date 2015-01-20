package nmct.howest.be.rideshare.Fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import nmct.howest.be.rideshare.Helpers.Utils;
import nmct.howest.be.rideshare.Loaders.Database.Contract;
import nmct.howest.be.rideshare.Loaders.Json.ProfileLoader;
import nmct.howest.be.rideshare.Models.Review;
import nmct.howest.be.rideshare.Models.User;
import nmct.howest.be.rideshare.R;


public class ProfileFragment extends Fragment implements LoaderManager.LoaderCallbacks<User> {

    //Variables
    private User CurrentUser;
    private View mHeader;
    private String UserId;
    private ImageView profilePictureView;
    private TextView txtName;
    private TextView txtPlace;
    private TextView txtGenderAge;
    private TextView txtCar;
    private TextView txtUserName;
    private List<Review> reviews = new ArrayList<Review>();
    private ProgressBar mProgressBar;
    private RelativeLayout mLayoutOtherProfile;

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
    public void onResume() {
        super.onResume();

        reviews.clear();

        //Restart loader to get data
        getLoaderManager().restartLoader(USER_LOADER_ID, null, this).forceLoad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_other_profile, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarOtherProfile);
        mReviewRecyclerView = (RecyclerView) view.findViewById(R.id.lstProfileReviews);

        // Setting the LayoutManager.
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mReviewRecyclerView.setLayoutManager(mLayoutManager);

        mHeader = LayoutInflater.from(getActivity()).inflate(R.layout.header_other_profile, mReviewRecyclerView, false);

        Button btnGo=(Button) mHeader.findViewById(R.id.btnOtherProfileReview);
        btnGo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                itemRatingPopup();
            }
        });

        // Setting the adapter.
        mReviewRecyclerAdapter = new ReviewRecyclerAdapter(mHeader, reviews);
        mReviewRecyclerView.setAdapter(mReviewRecyclerAdapter);

        return view;
    }

    //Implementation of ProfileLoader
    @Override
    public Loader<User> onCreateLoader(int i, Bundle bundle) {
        return new ProfileLoader(getActivity(), getResources().getString(R.string.API_Profile) + UserId );
    }

    @Override
    public void onLoadFinished(Loader<User> Loader, User user) {

        fillData(user);

        //Make layout visible when loaded and remove progressbar
        mProgressBar.setVisibility(View.INVISIBLE);
        mReviewRecyclerView.setVisibility(View.VISIBLE);

        getActivity().setTitle(user.getUserName());

        if (user.getReviews() != null) {
            reviews.addAll(user.getReviews());

            //Sort list so it's always the same
            Collections.sort(reviews, Collections.reverseOrder(new Review.compareToDate()));

            mReviewRecyclerAdapter.updateList(mHeader, reviews);
        }
    }

    @Override
    public void onLoaderReset(Loader<User> Loader) {
        reviews.clear();
        mReviewRecyclerAdapter.updateList(mHeader, reviews);
    }

    private void fillData(User user) {

        txtName = (TextView) mHeader.findViewById(R.id.txtOtherName);
        txtPlace = (TextView) mHeader.findViewById(R.id.txtOtherPlace);
        txtGenderAge = (TextView) mHeader.findViewById(R.id.txtOtherGenderAge);
        txtCar = (TextView) mHeader.findViewById(R.id.txtOtherCar);
        txtUserName = (TextView) mHeader.findViewById(R.id.txtOtherUserName);
        profilePictureView = (ImageView) mHeader.findViewById(R.id.imgOtherProfilePicture);

        txtName.setText(user.getFirstName() + " " + user.getLastName());

        if (TextUtils.isEmpty(user.getLocation())) {
            txtPlace.setText(getActivity().getResources().getString(R.string.locationUnknown));
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
            txtGenderAge.setText(getActivity().getResources().getString(R.string.ageUnknown));
        }

        if (!TextUtils.isEmpty(user.getCarType()) && !TextUtils.isEmpty(user.getAmountOfSeats())) {
            txtCar.setText(user.getCarType() + " (" + user.getAmountOfSeats() + getActivity().getResources().getString(R.string.placesShort));
        } else if (!TextUtils.isEmpty(user.getCarType()) && TextUtils.isEmpty(user.getAmountOfSeats())) {
            txtCar.setText(user.getCarType());
        } else if (TextUtils.isEmpty(user.getCarType()) && !TextUtils.isEmpty(user.getAmountOfSeats())) {
            txtCar.setText(user.getAmountOfSeats() + getActivity().getResources().getString(R.string.places));
        } else {
            txtCar.setText(getActivity().getResources().getString(R.string.carUnknown));
        }

//        if (!TextUtils.isEmpty(user.getFacebookID()))
//            profilePictureView.setProfileId(user.getFacebookID());
//        profilePictureView.setCropped(true);
        profilePictureView.setImageBitmap(Utils.getRoundedBitmap(user.getBitmapFb()));
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
                String reviewText = text.getText().toString().trim();
                Review review = new Review(CurrentUser.getID(), CurrentUser.getUserName(), date, rating, reviewText);
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String token = pref.getString("accessToken", "");
                APIHelper.AddReview(token,review,UserId);
                Log.d("time", text.getText().toString()+ " " + rating);
                popDialog.dismiss();

                //Restart loader to get data
                reviews.clear();
                getLoaderManager().restartLoader(USER_LOADER_ID, null, ProfileFragment.this).forceLoad();
            }
        });
        popDialog.show();
    }

    //Get current user from DB
    private LoaderManager.LoaderCallbacks<Cursor> CurrentUserLoader = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getActivity(), Contract.User.CONTENT_URI, new String[]
                    {
                            Contract.User.KEY_API_ID, Contract.User.KEY_USERNAME
                    }, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            CurrentUser = new User();
            while(cursor.moveToNext())
            {
                CurrentUser.setID(cursor.getString(cursor.getColumnIndex(Contract.User.KEY_API_ID)));
                CurrentUser.setUserName(cursor.getString(cursor.getColumnIndex(Contract.User.KEY_USERNAME)));
            }
            cursor.close();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {}
    };

}
