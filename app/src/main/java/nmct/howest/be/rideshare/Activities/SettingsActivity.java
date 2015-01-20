package nmct.howest.be.rideshare.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import nmct.howest.be.rideshare.Helpers.APIHelper;
import nmct.howest.be.rideshare.Helpers.Utils;
import nmct.howest.be.rideshare.R;
import nmct.howest.be.rideshare.RideshareApp;

public class SettingsActivity extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (findViewById(R.id.fragment_container_settings) != null) {

            SettingsFragment settingsFragment = new SettingsFragment();

            // Add the fragment to the 'fragment_container' Layout
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container_settings, settingsFragment);
            ft.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragment
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RideshareApp.getAppContext());
        String token = prefs.getString("accessToken", "");

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.basic_preferences);

            //Set new language
            Preference pref_language = findPreference("language");
            pref_language.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
            {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue)
                {
                    Utils.changeLanguage(newValue.toString());

                    //Restart app
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                }
            });

            //Delete my profile
            Preference pref_delete_profile = findPreference("delete_profile");
            pref_delete_profile.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
            {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getActivity().getResources().getString(R.string.dialogDeleteProfileTitle));
                    builder.setMessage(getActivity().getResources().getString(R.string.dialogDeleteProfile));
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Delete profile
                            APIHelper.DeleteProfile(token);

                            //Delete token from sharedPreferences and return to login screen
                            prefs.edit().remove("accessToken").apply();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
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
            });

            //Show credits
            Preference pref_credits = findPreference("credits");
            pref_credits.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    //Show credits dialog
                    final Dialog popDialog = new Dialog(getActivity());
                    popDialog.setContentView(R.layout.dialog_credits);
                    popDialog.setTitle("Credits");
                    popDialog.show();
                    return true;
                }
            });

        }
    }

}
