package nmct.howest.be.rideshare.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import nmct.howest.be.rideshare.R;

public class ProfileActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
                //MainActivity.pager.setCurrentItem(3, true);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
