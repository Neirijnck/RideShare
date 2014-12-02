package nmct.howest.be.rideshare.Activities.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import java.util.Calendar;

import nmct.howest.be.rideshare.Activities.Loaders.ProfileLoader;
import nmct.howest.be.rideshare.Activities.ProfileActivity;
import nmct.howest.be.rideshare.R;

public class MyProfileFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private CursorAdapter mAdapter;
    private TextView Naam;
    private TextView Plaats;
    private TextView GeslachtLeeftijd;
    public MyProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //Init loader to get data
        getLoaderManager().initLoader(1, null, this);
            }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        Naam = (TextView) view.findViewById(R.id.txtNaam);
        Plaats = (TextView) view.findViewById(R.id.txtPlaats);
        GeslachtLeeftijd = (TextView) view.findViewById(R.id.txtGeslachtLeeftijd);

        Session mFacebookSession = Session.getActiveSession();
        if(mFacebookSession.isOpened()){
            Request.executeMeRequestAsync(mFacebookSession, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        // got user graph
                        Naam.setText(user.getFirstName() + " "+ user.getLastName());
                         //Plaats.setText(user.getLocation().toString());
                         String verjaardag = user.getBirthday();

                         GeslachtLeeftijd.setText(user.asMap().get("gender").toString() + " " +
                            getAge(Integer.parseInt(verjaardag.substring(6,verjaardag.length())),
                                   Integer.parseInt(verjaardag.substring(0,2)),
                                   Integer.parseInt(verjaardag.substring(3,5))) +
                            " jaar"

                         );


                    } else {
                        // could not get user graph
                        Log.d("test", "niet gelukt");
                    }
                }
            });
        }
        return view;

    }


    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_with_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_settings:
                return true;
            case R.id.action_edit:
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //Implementation of ProfileLoader
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new ProfileLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (cursor.moveToFirst()){
            do{
                String firstName = cursor.getString(cursor.getColumnIndex("firstName"));
                String lastName = cursor.getString(cursor.getColumnIndex("lastName"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
            }while(cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }



}
