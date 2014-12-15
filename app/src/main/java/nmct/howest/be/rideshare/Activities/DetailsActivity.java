package nmct.howest.be.rideshare.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import nmct.howest.be.rideshare.Activities.Fragments.DetailMatchFragment;
import nmct.howest.be.rideshare.Activities.Fragments.DetailRequestTripFragment;
import nmct.howest.be.rideshare.Activities.Fragments.DetailRequestedTripFragment;
import nmct.howest.be.rideshare.Activities.Fragments.DetailSavedTripFragment;
import nmct.howest.be.rideshare.R;

public class DetailsActivity extends ActionBarActivity {

    private String id = "";
    private Integer type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        id = b.getString("id");
        type = b.getInt("type");

        // Check that the activity is using the layout version with
        // the fragment_container_details FrameLayout
        if (findViewById(R.id.fragment_container_details) != null)
        {
            // Add the fragment to the 'fragment_container_details' Layout
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            //Get the correct Details fragment
            switch(type) {
                case 0:
                    DetailRequestedTripFragment detailRequestedTripFragment = new DetailRequestedTripFragment().newInstance(id);
                    ft.add(R.id.fragment_container_details, detailRequestedTripFragment);
                    break;
                case 1:
                    DetailRequestTripFragment detailRequestsTripsFragment = new DetailRequestTripFragment().newInstance(id);
                    ft.add(R.id.fragment_container_details, detailRequestsTripsFragment);
                    break;
                case 2:
                    DetailSavedTripFragment detailSavedTripFragment = new DetailSavedTripFragment().newInstance(id);
                    ft.add(R.id.fragment_container_details, detailSavedTripFragment);
                    break;
                case 3:
                    DetailMatchFragment detailMatchFragment = new DetailMatchFragment().newInstance(id);
                    ft.add(R.id.fragment_container_details, detailMatchFragment);
                    break;
            }

            ft.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
