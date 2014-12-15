package nmct.howest.be.rideshare.Activities;

import android.app.AlertDialog;
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

import com.astuetz.PagerSlidingTabStrip;
import com.facebook.Session;

import nmct.howest.be.rideshare.Activities.Adapters.TabPagerAdapter;
import nmct.howest.be.rideshare.R;

public class MainActivity extends ActionBarActivity {

    //Tab variables
    public static ViewPager pager;
    private TabPagerAdapter TabAdapter;
    private PagerSlidingTabStrip tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAppCount();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        String token = pref.getString("accessToken", "");

        if(token == null || token == "") {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setting pageradapter
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabStrip);
        TabAdapter = new TabPagerAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(TabAdapter);

        // Bind the tabs to the ViewPager
        tabs.setViewPager(pager);
        tabs.setTextColor(getResources().getColor(R.color.rideshare_secondary));


        //Not necessary with our pagerslidingtabstrip
/*
        tabs.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar = getActionBar();
                actionBar.setSelectedNavigationItem(position);
            }
        });


       //Enable tabs as navigation
        actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            ActionBar.TabListener tabListener = new ActionBar.TabListener() {
                @Override
                public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                    pager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

                }

                @Override
                public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

                }
            };

            //Add our tabs
            actionBar.addTab(actionBar.newTab().setText(getString(R.string.Plan)).setTabListener(tabListener));
            actionBar.addTab(actionBar.newTab().setText(getString(R.string.Search)).setTabListener(tabListener));
            actionBar.addTab(actionBar.newTab().setText(getString(R.string.Trips)).setTabListener(tabListener));
            actionBar.addTab(actionBar.newTab().setText(getString(R.string.Profile)).setTabListener(tabListener));
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

    /**
     * Logout From Facebook
     */
    private void callFacebookLogout(Context context) {
        Session session = Session.getActiveSession();
        if (session != null) {
            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
            }
        }
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


    private void checkAppCount(){
        super.onStart();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor edt = pref.edit();
        int count = pref.getInt("count", 0);
        count ++;
        if(count == 5)
        {


            new AlertDialog.Builder(this)
                    .setTitle("Please donate!")
                    .setMessage("Do you find this application useful? Support it's development by sending donation to the developer.")
                    .setPositiveButton("Donate", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://188.226.154.228:8080/donate"));
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
                            Uri data = Uri.parse("mailto:shareride@gmail.com");
                            intent.setData(data);
                            startActivity(intent);
                        }
                    })

                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();


            count = 0;
        }
        edt.putInt("count", count );
        edt.commit();
        Log.d("count",""+count);


    }


}
