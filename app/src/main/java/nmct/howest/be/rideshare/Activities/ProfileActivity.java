package nmct.howest.be.rideshare.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import nmct.howest.be.rideshare.Helpers.Utils;
import nmct.howest.be.rideshare.R;

public class ProfileActivity extends ActionBarActivity {

    //Variables
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        String language = prefs.getString("language", "");

        //Set Language
        Utils.changeLanguage(language);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.home:
                return true;
            case 16908332:
                MainActivity.pager.setCurrentItem(3, true);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
