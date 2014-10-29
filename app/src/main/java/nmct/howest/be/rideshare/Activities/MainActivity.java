package nmct.howest.be.rideshare.Activities;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import nmct.howest.be.rideshare.Activities.Adapters.TabPagerAdapter;
import nmct.howest.be.rideshare.R;


public class MainActivity extends FragmentActivity {

    //Tab variables
    private ViewPager Tab;
    private TabPagerAdapter TabAdapter;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting pageradapter
        TabAdapter = new TabPagerAdapter(getSupportFragmentManager());
        Tab = (ViewPager)findViewById(R.id.pager);
        Tab.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position) {
                actionBar = getActionBar();
                actionBar.setSelectedNavigationItem(position);
            }
        });
        Tab.setAdapter(TabAdapter);
        actionBar = getActionBar();

        //Enable tabs as navigation
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener()
        {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                Tab.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }
        };

        //Add our tabs
        actionBar.addTab(actionBar.newTab().setText("Plannen").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("Zoeken").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("Ritten").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("Profiel").setTabListener(tabListener));

    }


    //Menu is per fragment
/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


}
