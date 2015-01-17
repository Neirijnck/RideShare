package nmct.howest.be.rideshare.Fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.widget.ProfilePictureView;

import nmct.howest.be.rideshare.Helpers.APIHelper;
import nmct.howest.be.rideshare.R;
import nmct.howest.be.rideshare.RideshareApp;

public class EditProfileFragment extends Fragment
{
    //Variables
    private String firstName;
    private String lastName;
    private String userName;
    private String location;
    private String carType;
    private String places;
    private String facebookID;

    private EditText txtFirstName;
    private EditText txtLastName;
    private EditText txtUserName;
    private EditText txtLocation;
    private EditText txtCarType;
    private EditText txtPlaces;

    private Button btnSaveProfile;
    private ProfilePictureView imgEditProfilePic;

    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(RideshareApp.getAppContext());
    String token = pref.getString("accessToken", "");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getActivity().getIntent().getExtras();
        firstName = b.getString("firstName");
        lastName = b.getString("lastName");
        userName = b.getString("userName");
        location = b.getString("location");
        carType = b.getString("carType");
        places = b.getString("places");
        facebookID = b.getString("facebookID");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.title_fragment_edit_profile));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        txtFirstName = (EditText) v.findViewById(R.id.txtEditFirstName);
        txtLastName = (EditText) v.findViewById(R.id.txtEditName);
        txtUserName = (EditText) v.findViewById(R.id.txtEditUserName);
        txtLocation = (EditText) v.findViewById(R.id.txtEditPlace);
        txtCarType = (EditText) v.findViewById(R.id.txtEditCarType);
        txtPlaces = (EditText) v.findViewById(R.id.txtEditPlaces);
        imgEditProfilePic = (ProfilePictureView) v.findViewById(R.id.imgEditProfilePic);

        txtFirstName.setText(firstName);
        txtLastName.setText(lastName);
        txtUserName.setText(userName);
        txtLocation.setText(location);
        txtCarType.setText(carType);
        txtPlaces.setText(places);
        imgEditProfilePic.setProfileId(facebookID);

        btnSaveProfile = (Button) v.findViewById(R.id.btnEditSaveProfile);
        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //When clicking on save, update the profile info in database
                firstName = txtFirstName.getText().toString();
                lastName = txtLastName.getText().toString();
                userName = txtUserName.getText().toString();
                location = txtLocation.getText().toString();
                carType = txtCarType.getText().toString();
                places = txtPlaces.getText().toString();

                //Check if empty!
                if(TextUtils.isEmpty(firstName)||TextUtils.isEmpty(lastName)||TextUtils.isEmpty(userName)||TextUtils.isEmpty(location))
                {
                    Toast.makeText(getActivity(), "Vul alle verplichte velden in", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Cartype and amount of seats are empty
                    if(TextUtils.isEmpty(carType)&&TextUtils.isEmpty(places))
                    {
                        APIHelper.EditUser(userName.trim(), firstName.trim(), lastName.trim(), token, location.trim());
                    }
                    else if(TextUtils.isEmpty(places)&&!TextUtils.isEmpty(carType))
                    {
                        APIHelper.EditUser(userName.trim(), firstName.trim(), lastName.trim(), token, location.trim(), carType.trim());
                    }
                    else if(!TextUtils.isEmpty(places)&&TextUtils.isEmpty(carType))
                    {
                        int amount=1;
                        try{ amount = Integer.parseInt(places);}
                        catch (NumberFormatException  ex){Toast.makeText(getActivity(), "Aantal plaatsen moet een getal zijn", Toast.LENGTH_SHORT).show();}

                        APIHelper.EditUser(userName.trim(), firstName.trim(), lastName.trim(), token, location.trim(), amount);
                    }
                    else{APIHelper.EditUser(userName.trim(), firstName.trim(), lastName.trim(), token, location.trim(), carType.trim(), places.trim());}
                    //MainActivity.pager.setCurrentItem(3, true);
                    getActivity().finish();

                }
            }
        });

        return v;
    }

}
