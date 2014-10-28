package nmct.howest.be.rideshare.Activities.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import nmct.howest.be.rideshare.Activities.Fragments.MijnProfielFragment;
import nmct.howest.be.rideshare.Activities.Fragments.PlannenFragment;
import nmct.howest.be.rideshare.Activities.Fragments.RittenFragment;
import nmct.howest.be.rideshare.Activities.Fragments.ZoekenFragment;

/**
 * Created by Preben on 27/10/2014.
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter
{
    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                //Plannen Fragment
                return new PlannenFragment();
            case 1:
                //Zoeken Fragment
                return new ZoekenFragment();
            case 2:
                //Ritten Fragment
                return new RittenFragment();
            case 3:
                //Profiel Fragment
                return new MijnProfielFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        //Number of tabs
        return 4;
    }
}
