package nmct.howest.be.rideshare.Activities.Fragments;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import nmct.howest.be.rideshare.Activities.Loaders.TripLoader;
import nmct.howest.be.rideshare.R;

public class RittenFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private CursorAdapter mAdapter;

    public RittenFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_ritten, container, false);

        //List ophalen en opvullen
        ListView list = (ListView) view.findViewById(R.id.lstRitAanvragen);
        fillData(list);

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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Method to fill the data in UI
    private void fillData(ListView list)
    {
        //Init loader to get data
        getLoaderManager().initLoader(0, null, this);
        // Fields on the UI to which we map
        String[] columns = new String[]{"trip"};
        int[] views = new int[]{R.id.txbBeoordeling};
        //Setting cursoradapter
        mAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null, columns, views,0);
        list.setAdapter(mAdapter);
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
    //mAdapter.swapCursor(cursor);
}

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader)
    {
        mAdapter.swapCursor(null);
    }
}
