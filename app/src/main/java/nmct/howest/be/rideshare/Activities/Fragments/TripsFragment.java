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
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import nmct.howest.be.rideshare.Activities.Adapters.TripRequestAdapter;
import nmct.howest.be.rideshare.Activities.Adapters.TripRequestedAdapter;
import nmct.howest.be.rideshare.Activities.Adapters.TripSavedAdapter;
import nmct.howest.be.rideshare.Activities.Loaders.Json.TripLoader;
import nmct.howest.be.rideshare.Activities.MainActivity;
import nmct.howest.be.rideshare.Activities.Models.Review;
import nmct.howest.be.rideshare.Activities.Models.Trip;
import nmct.howest.be.rideshare.Activities.SearchActivity;
import nmct.howest.be.rideshare.R;

public class TripsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Trip>>
{
    private List<Trip> mTrips;

    private ArrayAdapter mAdapterTripSaved;
    private ListView listMyTrips;

    private ArrayAdapter mAdapterTripRequest;
    private ListView listRequestTrips;

    private ArrayAdapter mAdapterTripRequested;
    private ListView listRequestedTrips;

    public TripsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //Init loader to get data
        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trips, container, false);


        FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.pager.setCurrentItem(0);
            }
        });


        //Lists ophalen en opvullen
        listMyTrips = (ListView) view.findViewById(R.id.lstOpgeslagenRitten);
        listRequestTrips = (ListView) view.findViewById(R.id.lstRitAanvragen);
        listRequestedTrips = (ListView) view.findViewById(R.id.lstRitVerzoeken);

        //Setting adapters
        mAdapterTripSaved = new TripSavedAdapter(getActivity(), R.layout.card_trip, R.id.txtTripInfo1);
        mAdapterTripRequest = new TripRequestAdapter(getActivity(), R.layout.card_trip, R.id.txtTripInfo1);
        mAdapterTripRequested = new TripRequestedAdapter(getActivity(), R.layout.card_trip, R.id.txtTripInfo1);

        listMyTrips.setAdapter(mAdapterTripSaved);


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

    //Implementation of the triploader
    @Override
    public Loader<List<Trip>> onCreateLoader(int i, Bundle bundle)
    {
        String url = "http://188.226.154.228:8080/api/v1/trips";
        return new TripLoader(getActivity(), url );
    }

    @Override
    public void onLoadFinished(Loader<List<Trip>> cursorLoader, List<Trip> trips)
    {
        mTrips = trips;
        mAdapterTripSaved.addAll(mTrips);
    }

    @Override
    public void onLoaderReset(Loader<List<Trip>> cursorLoader)
    {
        mTrips.clear();
    }
}
