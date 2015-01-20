package nmct.howest.be.rideshare.Activities;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.facebook.Session;

import nmct.howest.be.rideshare.Account.AccountUtils;
import nmct.howest.be.rideshare.Adapters.TabPagerAdapter;
import nmct.howest.be.rideshare.Helpers.Utils;
import nmct.howest.be.rideshare.Loaders.Database.Contract;
import nmct.howest.be.rideshare.R;

public class MainActivity extends ActionBarActivity {

    //Variables
    SharedPreferences prefs;

    //Tab variables
    public static ViewPager pager;
    private TabPagerAdapter TabAdapter;
    private PagerSlidingTabStrip tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (getIntent() != null && getIntent().getExtras() != null) {
            Integer p = getIntent().getExtras().getInt("PAGE", 0);
            String msg = getIntent().getExtras().getString("TOAST", "");
            pager.setCurrentItem(p, true);

            if (!msg.isEmpty()) {
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            }
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        String token = prefs.getString("accessToken", "");
        String reg_id = prefs.getString("REG_ID", "");
        String language = prefs.getString("language", "");

        //Set Language
        Utils.changeLanguage(language);

        if (token.isEmpty() || reg_id.isEmpty()) {
            callFacebookLogout(this);
            return;
        }

        checkAppCount();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setting pageradapter
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabStrip);
        TabAdapter = new TabPagerAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(TabAdapter);

        //Bind the tabs to the ViewPager
        tabs.setViewPager(pager);
        tabs.setTextColor(getResources().getColor(R.color.rideshare_secondary));

        //Set up a sync account if needed
        if (!AccountUtils.isAccountExists(this)) {
            AccountUtils.CreateSyncAccount(this);
        }

        //Set sync to automatically
//        ContentResolver.setSyncAutomatically(AccountUtils.getAccount(this), Contract.AUTHORITY, true);

        //Request sync
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(AccountUtils.getAccount(this), Contract.AUTHORITY, settingsBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*if (getIntent() != null && getIntent().getExtras() != null) {
            Integer p = getIntent().getExtras().getInt("PAGE", 0);
            String msg = getIntent().getExtras().getString("TOAST", "");
            pager.setCurrentItem(p, true);

            if (!msg.isEmpty()) {
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            }
        }*/
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                callFacebookLogout(this.getApplicationContext());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Logout From Facebook
    private void callFacebookLogout(Context context) {
        Session session = Session.getActiveSession();
        if (session != null) {
            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
            }
        }
        prefs.edit().remove("accessToken").commit();
        //prefs.edit().remove("REG_ID").commit();
        Intent intent = new Intent(this.getApplicationContext(), LoginActivity.class);
        this.startActivity(intent);
    }

    //Close app on back button
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

    //Check how many times the app was opened
    private void checkAppCount() {
        super.onStart();
        SharedPreferences.Editor edt = prefs.edit();
        int count = prefs.getInt("count", 0);
        count++;
        if (count == 20) {
            new AlertDialog.Builder(this)
                    .setTitle("Please donate!")
                    .setMessage("Vind je deze app nuttig? Steun de ontwikkeling ervan door te doneren aan de ontwikkelaars.")
                    .setPositiveButton("Doneer", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.Site_Donate)));
                            startActivity(browserIntent);
                        }
                    })
                    .setNeutralButton("Rate", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                            }
                        }
                    })
                    .setNegativeButton("Email", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            Uri data = Uri.parse("mailto:sharemyride@gmail.com");
                            intent.setData(data);
                            startActivity(intent);
                        }
                    })

                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            count = 0;
        }
        edt.putInt("count", count);
        edt.commit();
        Log.d("count", "" + count);
    }

}