package nmct.howest.be.rideshare.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

import nmct.howest.be.rideshare.Fragments.ImageFragment;
import nmct.howest.be.rideshare.R;

public class ImagePagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter
{
    //Different images to show
    private int[] Images = new int[] { R.drawable.plannen, R.drawable.zoeken, R.drawable.ritten, R.drawable.profile, R.drawable.match};

    //Length of image array
    private int mCount = Images.length;

    public ImagePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    //Show certain fragment from position with image as parameter
    @Override
    public Fragment getItem(int position) {
        return new ImageFragment().newInstance(Images[position]);
    }

    @Override
    public int getIconResId(int index) {
        return 0;
    }

    @Override
    public int getCount() {
        return mCount;
    }

}
