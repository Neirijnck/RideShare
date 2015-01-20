package nmct.howest.be.rideshare.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.facebook.FacebookException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.WebDialog;

import nmct.howest.be.rideshare.Fragments.SearchResultsFragment;
import nmct.howest.be.rideshare.Helpers.Utils;
import nmct.howest.be.rideshare.R;

public class SearchActivity extends ActionBarActivity
{
    //Variables
    private String from="";
    private String fromPlaceid="";
    private String to="";
    private String toPlaceid="";
    private String date="";
    private String time="";
    private boolean share;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        String language = prefs.getString("language", "");

        //Set Language
        Utils.changeLanguage(language);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        from = b.getString("from");
        fromPlaceid = b.getString("fromPlaceid");
        to = b.getString("to");
        toPlaceid = b.getString("toPlaceid");
        date = b.getString("date");
        time = b.getString("time");
        share = b.getBoolean("share");

        if(share){
            share(from, to, date, time);
        }

        //Add searchResultsFragment to the container
        if (findViewById(R.id.fragment_container_search) != null) {

            SearchResultsFragment searchResultsFragment = SearchResultsFragment.newInstance(from, fromPlaceid, to, toPlaceid ,date, time, share);

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

    //If checked, share on facebook
    private void share(String from, String to, final String date, String time) {

        final String van = from;
        final String tot = to;
        final String datum = date;
        final String tijd = time;
        Session.openActiveSession(this, true, new Session.StatusCallback() {

            @Override
            public void call(Session session, SessionState state, Exception exception) {
                if (session.isOpened()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("caption", getResources().getString(R.string.shareCaption));
                    bundle.putString("description",
                            getResources().getString(R.string.shareFirst)+ van +
                                    getResources().getString(R.string.shareSecond)+tot+
                                    getResources().getString(R.string.shareThird)+datum+
                                    getResources().getString(R.string.shareFourth)+ tijd);
                    bundle.putString("link", getResources().getString(R.string.Site));
                    bundle.putString("name", "Share My Ride");
                    bundle.putString("picture", getResources().getString(R.string.Site_Icon));
                    new WebDialog.FeedDialogBuilder(SearchActivity.this, Session.getActiveSession(), bundle).setOnCompleteListener(new WebDialog.OnCompleteListener()
                    {
                        @Override
                        public void onComplete(Bundle values, FacebookException error)
                        { }
                    }).build().show();
                }
            }
        });
    }

}
