package nmct.howest.be.rideshare.Activities.Fragments;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import nmct.howest.be.rideshare.Activities.Adapters.TripRequestCursorAdapter;
import nmct.howest.be.rideshare.Activities.Loaders.TripLoader;
import nmct.howest.be.rideshare.Activities.SearchActivity;
import nmct.howest.be.rideshare.R;

public class TripsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private CursorAdapter mAdapter;
    private ListView listAanvragen;

    public TripsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //Init loader to get data
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_trips, container, false);

        //List ophalen en opvullen
        listAanvragen = (ListView) view.findViewById(R.id.lstRitAanvragen);
        fillData();

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Method to fill the data in UI
    private void fillData()
    {
        //Setting cursoradapter
        mAdapter = new TripRequestCursorAdapter(getActivity(), null, 0);
        listAanvragen.setAdapter(mAdapter);
    }

    //Implementation of the triploader
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        return new TripLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor)
    {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader)
    {
        mAdapter.swapCursor(null);
    }
}
