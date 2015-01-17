package nmct.howest.be.rideshare.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import nmct.howest.be.rideshare.Activities.ProfileActivity;
import nmct.howest.be.rideshare.Adapters.ReviewRecyclerAdapter;
import nmct.howest.be.rideshare.Loaders.Json.ProfileLoader;
import nmct.howest.be.rideshare.Models.Review;
import nmct.howest.be.rideshare.Models.User;
import nmct.howest.be.rideshare.R;

public class MyProfileFragment extends Fragment implements LoaderManager.LoaderCallbacks<User> {

    //Variables
    public LoaderManager loaderManager;
    private static final int USER_LOADER_ID = 1;

    private User mUser;
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

    public MyProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        loaderManager = getLoaderManager();

        //Init loader to get data
        loaderManager.initLoader(USER_LOADER_ID, null, this).forceLoad();
    }

    @Override
    public void onResume() {
        super.onResume();

        reviews.clear();

        //Restart loader to get data
        loaderManager.restartLoader(USER_LOADER_ID, null, this).forceLoad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //lstReviews = (LinearLayout) view.findViewById(R.id.lstBeoordelingen);
        mReviewRecyclerView = (RecyclerView) view.findViewById(R.id.lstReviews);

        // Setting the LayoutManager.
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mReviewRecyclerView.setLayoutManager(mLayoutManager);

        // Setting the adapter.
        mReviewRecyclerAdapter = new ReviewRecyclerAdapter(reviews);
        mReviewRecyclerView.setAdapter(mReviewRecyclerAdapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_with_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_edit:
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                Bundle b = new Bundle();
                if (mUser != null) {
                    b.putString("firstName", mUser.getFirstName());
                    b.putString("lastName", mUser.getLastName());
                    b.putString("userName", mUser.getUserName());
                    b.putString("location", mUser.getLocation());
                    b.putString("carType", mUser.getCarType());
                    b.putString("places", mUser.getAmountOfSeats());
                    b.putString("facebookID", mUser.getFacebookID());
                }
                intent.putExtras(b);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Implementation of ProfileLoader
    @Override
    public Loader<User> onCreateLoader(int i, Bundle bundle) {
        return new ProfileLoader(getActivity(), getResources().getString(R.string.API_Profile));
    }

    @Override
    public void onLoadFinished(Loader<User> Loader, User user) {
        mUser = user;
        fillData(user);

        if (user.getReviews() != null) {
            reviews.addAll(user.getReviews());
            mReviewRecyclerAdapter.updateList(reviews);
        }

        //If list isn't empty, show the list
        if (!reviews.isEmpty()) {
            TextView txbNoReviews = (TextView) getActivity().findViewById(R.id.txbNoReviews);
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
        txtName = (TextView) getView().findViewById(R.id.txtNaam);
        txtPlace = (TextView) getView().findViewById(R.id.txtPlaats);
        txtGenderAge = (TextView) getView().findViewById(R.id.txtGeslachtLeeftijd);
        txtCar = (TextView) getView().findViewById(R.id.txtAuto);
        txtUserName = (TextView) getView().findViewById(R.id.txtUserName);
        profilePictureView = (ProfilePictureView) getView().findViewById(R.id.imgProfilePicture);

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
            Date date = sdf.parse(user.getBirthday());

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

}
