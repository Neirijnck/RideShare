package nmct.howest.be.rideshare.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.facebook.Session;

import nmct.howest.be.rideshare.Activities.Fragments.LoginFragment;
import nmct.howest.be.rideshare.R;

public class LoginActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null)
        {

            LoginFragment loginFragment = new LoginFragment();

            // Add the fragment to the 'fragment_container' Layout
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, loginFragment).commit();
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

}
