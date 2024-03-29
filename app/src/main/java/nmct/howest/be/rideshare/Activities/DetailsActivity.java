package nmct.howest.be.rideshare.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import nmct.howest.be.rideshare.Fragments.DetailMatchTripFragment;
import nmct.howest.be.rideshare.Fragments.DetailRequestTripFragment;
import nmct.howest.be.rideshare.Fragments.DetailRequestedTripFragment;
import nmct.howest.be.rideshare.Fragments.DetailSavedTripFragment;
import nmct.howest.be.rideshare.Helpers.Utils;
import nmct.howest.be.rideshare.R;

public class DetailsActivity extends ActionBarActivity {

    //Variables
    private String id = "";
    private String matchID="";
    private Integer type;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        String language = prefs.getString("language", "");

        //Set Language
        Utils.changeLanguage(language);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        id = b.getString("id");
        matchID = b.getString("matchID");
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
                    DetailRequestedTripFragment detailRequestedTripFragment = new DetailRequestedTripFragment().newInstance(id, matchID);
                    ft.replace(R.id.fragment_container_details, detailRequestedTripFragment);
                    break;
                case 1:
                    DetailRequestTripFragment detailRequestsTripsFragment = new DetailRequestTripFragment().newInstance(id, matchID);
                    ft.replace(R.id.fragment_container_details, detailRequestsTripsFragment);
                    break;
                case 2:
                    DetailSavedTripFragment detailSavedTripFragment = new DetailSavedTripFragment().newInstance(id);
                    ft.replace(R.id.fragment_container_details, detailSavedTripFragment);
                    break;
                case 3:
                    DetailMatchTripFragment detailMatchTripFragment = new DetailMatchTripFragment().newInstance(id);
                    ft.replace(R.id.fragment_container_details, detailMatchTripFragment);
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
            case 16908332:
                MainActivity.pager.setCurrentItem(2, true);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
