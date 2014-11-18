package nmct.howest.be.rideshare.Activities;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;

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
        tabs.setIndicatorColor(getResources().getColor(R.color.white));
        tabs.setIndicatorHeight(11);
        tabs.setTextColor(getResources().getColor(R.color.white));
        tabs.setUnderlineColor(getResources().getColor(R.color.rideshare_color));
        tabs.setUnderlineHeight(5);
        tabs.setDividerColor(getResources().getColor(R.color.rideshare_color));
        tabs.setAllCaps(true);
        tabs.setMinimumHeight(48);


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


}
