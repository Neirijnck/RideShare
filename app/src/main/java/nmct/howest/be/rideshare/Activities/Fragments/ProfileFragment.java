package nmct.howest.be.rideshare.Activities.Fragments;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import nmct.howest.be.rideshare.Activities.Adapters.ReviewAdapter;
import nmct.howest.be.rideshare.Activities.Helpers.APIHelper;
import nmct.howest.be.rideshare.Activities.Loaders.Json.ProfileLoader;
import nmct.howest.be.rideshare.Activities.Models.Review;
import nmct.howest.be.rideshare.Activities.Models.User;
import nmct.howest.be.rideshare.R;


public class ProfileFragment extends Fragment implements LoaderManager.LoaderCallbacks<User> {

    private User CurrentUser;
    private String UserId;
    private ProfilePictureView profilePictureView;
    private TextView txtName;
    private TextView txtPlace;
    private TextView txtGenderAge;
    private TextView txtCar;
    private TextView txtUserName;
    private LinearLayout lstReviews;
    private ArrayAdapter<Review> mAdapterReview;
    private ArrayList<Review> reviews;

    public ProfileFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle b = getActivity().getIntent().getExtras();
        UserId = b.getString("userID");

        //Init loader to get data
        getLoaderManager().initLoader(1, null, this).forceLoad();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_other_profile, container, false);

        lstReviews = (LinearLayout) view.findViewById(R.id.lstBeoordelingen);

        mAdapterReview = new ReviewAdapter(getActivity(), R.layout.card_review, R.id.txbBeoordelingNaam);
        TextView btnGo=(TextView) view.findViewById(R.id.btnOtherProfileReview);
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
        public void onLoaderReset(Loader<User> loader) {

        }
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
        reviews = new ArrayList<Review>();

        if (user.getReviews() != null) {
            reviews = (ArrayList) user.getReviews();
            mAdapterReview.addAll(reviews);
        }

        //If list isnt empty, show the list
        if (!reviews.isEmpty()) {
            TextView txbNoReviews = (TextView) getActivity().findViewById(R.id.txbNoReviews);
            txbNoReviews.setVisibility(View.INVISIBLE);

            LinearLayout layoutReviews = (LinearLayout) getActivity().findViewById(R.id.lstBeoordelingen);
            layoutReviews.setVisibility(View.VISIBLE);
        }

        //Adding items to linear layouts
        int adaptercountReviews = mAdapterReview.getCount();
        for (int i = 0; i < adaptercountReviews; i++) {
            View item = mAdapterReview.getView(i, null, null);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Get info from the item
                }
            });
            lstReviews.addView(item);
        }
    }

    @Override
    public void onLoaderReset(Loader<User> Loader) {
        reviews.clear();
        mAdapterReview.notifyDataSetChanged();
    }

    private void fillData(User user) {

        txtName = (TextView) getView().findViewById(R.id.txtOtherNaam);
        txtPlace = (TextView) getView().findViewById(R.id.txtOtherPlaats);
        txtGenderAge = (TextView) getView().findViewById(R.id.txtOtherGeslachtLeeftijd);
        txtCar = (TextView) getView().findViewById(R.id.txtOtherAuto);
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
    private void itemRatingPopup() {
        getLoaderManager().initLoader(2, null, CurrentUserLoader).forceLoad();
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
                Review review = new Review(CurrentUser.getID(), CurrentUser.getUserName(), date, rating, text.getText().toString());
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
