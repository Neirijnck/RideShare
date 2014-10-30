package nmct.howest.be.rideshare.Activities.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import nmct.howest.be.rideshare.Activities.Fragments.MijnProfielFragment;
import nmct.howest.be.rideshare.Activities.Fragments.PlannenFragment;
import nmct.howest.be.rideshare.Activities.Fragments.RittenFragment;
import nmct.howest.be.rideshare.Activities.Fragments.ZoekenFragment;

public class TabPagerAdapter extends FragmentStatePagerAdapter
{
    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                //Plan Fragment
                return new PlannenFragment();
            case 1:
                //Search Fragment
                return new ZoekenFragment();
            case 2:
                //Trips Fragment
                return new RittenFragment();
            case 3:
                //Profile Fragment
                return new MijnProfielFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;   //Number of tabs
    }
}
