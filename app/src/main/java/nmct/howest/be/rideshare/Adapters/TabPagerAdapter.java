package nmct.howest.be.rideshare.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import nmct.howest.be.rideshare.Fragments.MyProfileFragment;
import nmct.howest.be.rideshare.Fragments.PlanFragment;
import nmct.howest.be.rideshare.Fragments.SearchFragment;
import nmct.howest.be.rideshare.Fragments.TripsFragment;
import nmct.howest.be.rideshare.R;
import nmct.howest.be.rideshare.RideshareApp;

public class TabPagerAdapter extends FragmentStatePagerAdapter
{
    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private final String[] TITLES = {
            RideshareApp.getAppContext().getResources().getString(R.string.Plan),
            RideshareApp.getAppContext().getResources().getString(R.string.Search),
            RideshareApp.getAppContext().getResources().getString(R.string.Trips),
            RideshareApp.getAppContext().getResources().getString(R.string.Profile)
    };

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                //Plan Fragment
                return new PlanFragment();
            case 1:
                //Search Fragment
                return new SearchFragment();
            case 2:
                //Trips Fragment
                return new TripsFragment();
            case 3:
                //Profile Fragment
                return new MyProfileFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return TITLES.length;   //Number of tabs
    }

}
