package nmct.howest.be.rideshare.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import nmct.howest.be.rideshare.Activities.Fragments.DetailRequestFragment;
import nmct.howest.be.rideshare.R;

public class DetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Check that the activity is using the layout version with
        // the fragment_container_details FrameLayout
        if (findViewById(R.id.fragment_container_details) != null)
        {
            //Get the correct Details fragment
            DetailRequestFragment detailFragment = new DetailRequestFragment();

            // Add the fragment to the 'fragment_container_details' Layout
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_details, detailFragment).commit();
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
