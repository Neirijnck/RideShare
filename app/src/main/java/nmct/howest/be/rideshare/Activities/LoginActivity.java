package nmct.howest.be.rideshare.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.facebook.Session;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import nmct.howest.be.rideshare.Fragments.LoginFragment;
import nmct.howest.be.rideshare.Helpers.Utils;
import nmct.howest.be.rideshare.R;

public class LoginActivity extends FragmentActivity {

    //Variables
    //GCM
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    String SENDER_ID = "451680909206";
    GoogleCloudMessaging gcm;
    SharedPreferences prefs;
    Context context;
    String regid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        context = getApplicationContext();

        prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        String language = prefs.getString("language", "");

        //Set Language
        Utils.changeLanguage(language);

        // Check device for Play Services APK.
        if(Utils.isNetworkAvailable(context)) {
            if (checkPlayServices()) {
                gcm = GoogleCloudMessaging.getInstance(this);
                regid = getRegistrationId();

                if (regid.isEmpty()) {
                    registerInBackground();
                }
            }
        }

        //Facebook session check
        Session session = Session.getActiveSession();
        if(session==null){
            session = Session.openActiveSessionFromCache(this.getApplicationContext());
        }
        if(session!=null && session.isOpened()){
            Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.getApplicationContext().startActivity(intent);
        }
        else {
            // Check that the activity is using the layout version with
            // the fragment_container FrameLayout
            if (findViewById(R.id.fragment_container) != null) {

                LoginFragment loginFragment = new LoginFragment();

                // Add the fragment to the 'fragment_container' Layout
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, loginFragment).commit();
            }
        }
    }

    //Close app on pushing button back
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    //Notifications
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    private String getRegistrationId() {
        String registrationId = prefs.getString("REG_ID", "");
        if (registrationId.isEmpty()) {
            Log.i("", "Registration not found.");
            return "";
        }

        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new version.
        /*int registeredVersion = prefs.getInt("APP_VERSION", Integer.MIN_VALUE);
        int currentVersion = Utils.getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i("", "App version changed.");
            return "";
        }*/
        return registrationId;
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }

                    regid = gcm.register(SENDER_ID);
                    Log.e("RegId","Device registered as " + regid);

                    // Save or/and send the registration ID to your server
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("REG_ID", regid);
                    edit.commit();

                    return  regid;

                } catch (IOException ex) {
                    Log.e("Error", ex.getMessage());
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                    return "Fails";
                }
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.d("", "");
            }
        }.execute(null, null, null);
    }

}