package nmct.howest.be.rideshare.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import nmct.howest.be.rideshare.Activities.Fragments.DetailAanvraagFragment;
import nmct.howest.be.rideshare.R;

public class DetailsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Check that the activity is using the layout version with
        // the fragment_container_details FrameLayout
        if (findViewById(R.id.fragment_container_details) != null)
        {
            //Get the correct Details fragment
            DetailAanvraagFragment detailFragment = new DetailAanvraagFragment();

            // Add the fragment to the 'fragment_container_details' Layout
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_details, detailFragment).commit();
        }

    }

}
