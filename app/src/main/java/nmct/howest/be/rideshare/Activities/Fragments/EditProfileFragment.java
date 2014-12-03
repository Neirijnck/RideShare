package nmct.howest.be.rideshare.Activities.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import nmct.howest.be.rideshare.Activities.Helpers.APIHelper;
import nmct.howest.be.rideshare.Activities.MainActivity;
import nmct.howest.be.rideshare.R;

public class EditProfileFragment extends Fragment
{
    private String firstName;
    private String lastName;
    private String userName;
    private String location;
    private String carType;
    private String places;

    private EditText txtFirstName;
    private EditText txtLastName;
    private EditText txtUserName;
    private EditText txtLocation;
    private EditText txtCarType;
    private EditText txtPlaces;

    private Button btnSaveProfile;

    public EditProfileFragment() {

    }

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

        txtFirstName.setText(firstName);
        txtLastName.setText(lastName);
        txtUserName.setText(userName);
        txtLocation.setText(location);
        txtCarType.setText(carType);
        txtPlaces.setText(places);

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
                if(TextUtils.isEmpty(firstName)||TextUtils.isEmpty(lastName)||TextUtils.isEmpty(userName)||TextUtils.isEmpty(location)||TextUtils.isEmpty(carType)||TextUtils.isEmpty(places))
                {
                    Toast.makeText(getActivity(), "Vul alle velden in", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    APIHelper.EditUser(userName, firstName, lastName, "000", location, carType, places);
                    getActivity().finish();
                    MainActivity.pager.setCurrentItem(3, true);
                }
            }
        });

        return v;
    }
}
