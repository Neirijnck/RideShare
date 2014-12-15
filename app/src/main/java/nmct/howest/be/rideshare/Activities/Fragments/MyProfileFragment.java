package nmct.howest.be.rideshare.Activities.Fragments;

        import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import nmct.howest.be.rideshare.Activities.Adapters.ReviewAdapter;
import nmct.howest.be.rideshare.Activities.Loaders.Json.ProfileLoader;
import nmct.howest.be.rideshare.Activities.Models.Review;
import nmct.howest.be.rideshare.Activities.Models.User;
import nmct.howest.be.rideshare.Activities.ProfileActivity;
import nmct.howest.be.rideshare.R;

public class MyProfileFragment extends Fragment implements LoaderManager.LoaderCallbacks<User>
{
    private User mUser;
    private ProfilePictureView profilePictureView;
    private TextView txtName;
    private TextView txtPlace;
    private TextView txtGenderAge;
    private TextView txtCar;
    private TextView txtUserName;
    private LinearLayout lstReviews;
    private ArrayAdapter<Review> mAdapterReview;
    private ArrayList<Review> reviews;

    public MyProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //Init loader to get data
        getLoaderManager().initLoader(1, null, this).forceLoad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        lstReviews = (LinearLayout) view.findViewById(R.id.lstBeoordelingen);

        mAdapterReview = new ReviewAdapter(getActivity(), R.layout.card_review, R.id.txbBeoordelingNaam);

        //lstReviews.setAdapter(mAdapterReview);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_with_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_settings:
                return true;
            case R.id.action_edit:
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                Bundle b = new Bundle();
                if(mUser!=null)
                {
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
    public Loader<User> onCreateLoader(int i, Bundle bundle)
    {
        return new ProfileLoader(getActivity(), getResources().getString(R.string.API_Profile));
    }

    @Override
    public void onLoadFinished(Loader<User> Loader, User user)
    {
        mUser = user;
        fillData(user);

        reviews = new ArrayList<Review>();

        if(user.getReviews()!=null) {
            reviews = (ArrayList) user.getReviews();
            mAdapterReview.addAll(reviews);
        }

        //If list isnt empty, show the list
        if(!reviews.isEmpty())
        {
            TextView txbNoReviews = (TextView) getActivity().findViewById(R.id.txbNoReviews);
            txbNoReviews.setVisibility(View.INVISIBLE);

            LinearLayout layoutReviews = (LinearLayout) getActivity().findViewById(R.id.lstBeoordelingen);
            layoutReviews.setVisibility(View.VISIBLE);
        }

        //Adding items to linear layouts
        int adaptercountReviews = mAdapterReview.getCount();
        for(int i =0; i < adaptercountReviews; i++)
        {
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
    public void onLoaderReset(Loader<User> Loader)
    {
        reviews.clear();
        mAdapterReview.notifyDataSetChanged();
    }

    private void fillData(User user)
    {
        txtName = (TextView) getView().findViewById(R.id.txtNaam);
        txtPlace = (TextView) getView().findViewById(R.id.txtPlaats);
        txtGenderAge = (TextView) getView().findViewById(R.id.txtGeslachtLeeftijd);
        txtCar = (TextView) getView().findViewById(R.id.txtAuto);
        txtUserName = (TextView) getView().findViewById(R.id.txtUserName);
        profilePictureView = (ProfilePictureView) getView().findViewById(R.id.imgProfilePicture);

        txtName.setText(user.getFirstName() + " " + user.getLastName());

        if(TextUtils.isEmpty(user.getLocation()))
        {
            txtPlace.setText("Locatie niet bekend");
        }
        else
        {
            txtPlace.setText(user.getLocation());
        }
        txtUserName.setText(user.getUserName());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getDefault());
        String birthday="";
        try {
            Date date = sdf.parse(user.getBirthday());

            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
            birthday = fmtOut.format(date);
        }catch (ParseException ex){
            Log.e("ParseException Date", ex.getMessage());}
        txtGenderAge.setText(user.getGender() + ", " + birthday);


        if(!TextUtils.isEmpty(user.getCarType())&&!TextUtils.isEmpty(user.getAmountOfSeats())) {
            txtCar.setText(user.getCarType() + " (" + user.getAmountOfSeats() + " pl.)");
        }
        else if(!TextUtils.isEmpty(user.getCarType())&&TextUtils.isEmpty(user.getAmountOfSeats()))
        {
            txtCar.setText(user.getCarType());
        }
        else if(TextUtils.isEmpty(user.getCarType())&&!TextUtils.isEmpty(user.getAmountOfSeats()))
        {
            txtCar.setText(user.getAmountOfSeats() + " plaatsen");
        }
        else
        {
            txtCar.setText("Auto niet bekend");
        }
        if(!TextUtils.isEmpty(user.getFacebookID()))
            profilePictureView.setProfileId(user.getFacebookID());


        profilePictureView.setCropped(true);


    }


}
