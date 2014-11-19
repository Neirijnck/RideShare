package nmct.howest.be.rideshare.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.facebook.Session;

import nmct.howest.be.rideshare.Activities.Adapters.TabPagerAdapter;
import nmct.howest.be.rideshare.R;

public class MainActivity extends FragmentActivity {

    //Tab variables
    private ViewPager pager;
    private TabPagerAdapter TabAdapter;
    private PagerSlidingTabStrip tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting pageradapter
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabStrip);
        TabAdapter = new TabPagerAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(TabAdapter);

        // Bind the tabs to the ViewPager + properties
        tabs.setViewPager(pager);
        tabs.setBackgroundColor(getResources().getColor(R.color.rideshare_color));
        tabs.setTabBackground(R.drawable.tab_indicator_ab_rideshare);
        tabs.setIndicatorColor(getResources().getColor(R.color.teal));
        tabs.setIndicatorHeight(21);
        tabs.setTextColor(getResources().getColor(R.color.white));
        tabs.setUnderlineColor(getResources().getColor(R.color.rideshare_color));
        tabs.setUnderlineHeight(10);
        tabs.setDividerColor(getResources().getColor(R.color.rideshare_color));
        tabs.setAllCaps(true);

        //Facebook session check










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
                //uitloggen
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
    public static void callFacebookLogout(Context context) {
        Session session = Session.getActiveSession();
        if (session != null) {

            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                //clear your preferences if saved
            }
        } else {

            session = new Session(context);
            Session.setActiveSession(session);
            session.closeAndClearTokenInformation();
            //clear your preferences if saved

        }

    }


}
