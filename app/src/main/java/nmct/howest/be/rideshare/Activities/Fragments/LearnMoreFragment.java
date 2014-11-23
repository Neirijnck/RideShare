package nmct.howest.be.rideshare.Activities.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import nmct.howest.be.rideshare.Activities.Adapters.ImagePagerAdapter;
import nmct.howest.be.rideshare.R;

public class LearnMoreFragment extends Fragment
{
    private FragmentActivity context;
    private TextView getBack;
    private FragmentTransaction transaction;
    private LoginFragment loginFragment;

    public LearnMoreFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learnmore, container, false);

        getBack = (TextView) view.findViewById(R.id.txbGetBack);
        //Set click listener for "Learn more" textview
        getBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginFragment = new LoginFragment();
                slideDownFragment();
            }
        });


        //Set adapter to viewpager
        ViewPager gallery = (ViewPager) view.findViewById(R.id.gallery_view);
        gallery.setAdapter(new ImagePagerAdapter(getActivity().getSupportFragmentManager()));

        //Bind indicators to pager
        CirclePageIndicator indicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(gallery);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        context=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    private void slideDownFragment()
    {
        transaction = context.getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment
        transaction.setCustomAnimations(R.anim.enter_from_top, R.anim.exit_out_bottom);
        transaction.replace(R.id.fragment_container, loginFragment);

        // Commit the transaction
        transaction.commit();
    }

}
