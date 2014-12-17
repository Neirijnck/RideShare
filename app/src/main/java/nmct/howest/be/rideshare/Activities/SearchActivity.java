package nmct.howest.be.rideshare.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import nmct.howest.be.rideshare.Activities.Fragments.SearchResultsFragment;
import nmct.howest.be.rideshare.R;

public class SearchActivity extends ActionBarActivity
{
    private String from="";
    private String to="";
    private String date="";
    private String time="";
    private boolean share;

    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        from = b.getString("from");
        to = b.getString("to");
        date = b.getString("date");
        time = b.getString("time");
        share = b.getBoolean("share");

        //Add searchResultsFragment to the container
        if (findViewById(R.id.fragment_container_search) != null) {

            SearchResultsFragment searchResultsFragment = SearchResultsFragment.newInstance(from, to, date, time, share);

            // Add the fragment to the 'fragment_container' Layout
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container_search, searchResultsFragment);
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
                MainActivity.pager.setCurrentItem(1, true);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
