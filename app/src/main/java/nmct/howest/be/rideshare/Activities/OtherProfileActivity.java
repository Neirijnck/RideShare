package nmct.howest.be.rideshare.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import nmct.howest.be.rideshare.R;

public class OtherProfileActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

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
                this.finish();
                Log.d("test","gedrukt");
                return true;
            case 16908332:
                Log.d("test","gedrukt ezufizhiuh");
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
