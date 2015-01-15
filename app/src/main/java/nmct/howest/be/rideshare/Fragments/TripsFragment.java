package nmct.howest.be.rideshare.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.tonicartos.superslim.GridSectionLayoutManager;
import com.tonicartos.superslim.LayoutManager;
import com.tonicartos.superslim.LinearSectionLayoutManager;
import com.tonicartos.superslim.SectionLayoutManager;

import java.util.ArrayList;
import java.util.List;

import nmct.howest.be.rideshare.Activities.SearchActivity;
import nmct.howest.be.rideshare.Adapters.TripRecyclerAdapter;
import nmct.howest.be.rideshare.Loaders.Json.SpecialTripsLoader;
import nmct.howest.be.rideshare.Loaders.Json.TripsLoader;
import nmct.howest.be.rideshare.Models.Trip;
import nmct.howest.be.rideshare.R;

public class TripsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Trip>>
{
    private LoaderManager loaderManager;
    private static final int TRIPS_SAVED_LOADER_ID = 0;
    private static final int TRIPS_REQUESTS_LOADER_ID = 10;
    private static final int TRIPS_REQUESTED_LOADER_ID = 20;

    private List<Trip> mAllTrips = new ArrayList<Trip>();
    private List<Trip> mSavedTrips = new ArrayList<Trip>();
    private List<Trip> mRequestTrips = new ArrayList<Trip>();
    private List<Trip> mRequestedTrips = new ArrayList<Trip>();

    private RecyclerView mTripsRecyclerView;
    private TripRecyclerAdapter mTripRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private GridSectionLayoutManager mGridSectionLayoutManager;
    private SectionLayoutManager mLinearSectionLayoutManager;
    private LayoutManager.SlmFactory mSlmFactory = new LayoutManager.SlmFactory() {

        @Override
        public SectionLayoutManager getSectionLayoutManager(LayoutManager layoutManager,
                                                            int section) {
            int sectionKind = section % 2;
            final SectionLayoutManager slm;
            if (sectionKind == 0) {
                GridSectionLayoutManager grid = mGridSectionLayoutManager;
                slm = mGridSectionLayoutManager;
            } else {
                slm = mLinearSectionLayoutManager;
            }
            return slm;
        }
    };

    public TripsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        loaderManager = getLoaderManager();

        //Init loaders to get data
        loaderManager.initLoader(TRIPS_SAVED_LOADER_ID, null, this).forceLoad();
        loaderManager.initLoader(TRIPS_REQUESTS_LOADER_ID, null, this).forceLoad();
        loaderManager.initLoader(TRIPS_REQUESTED_LOADER_ID, null, this).forceLoad();
    }

    @Override
    public void onResume() {
        super.onResume();

        mAllTrips.clear();

        //Restart loaders to get data
        loaderManager.restartLoader(TRIPS_SAVED_LOADER_ID, null, this).forceLoad();
        loaderManager.restartLoader(TRIPS_REQUESTS_LOADER_ID, null, this).forceLoad();
        loaderManager.restartLoader(TRIPS_REQUESTED_LOADER_ID, null, this).forceLoad();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        LayoutManager lm = new LayoutManager();
        mGridSectionLayoutManager = new GridSectionLayoutManager(lm, getActivity());
        mGridSectionLayoutManager.setColumnMinimumWidth((int) getResources()
                .getDimension(R.dimen.grid_column_width));
        mLinearSectionLayoutManager = new LinearSectionLayoutManager(lm);
        lm.setSlmFactory(mSlmFactory);

        mTripsRecyclerView = (RecyclerView) view.findViewById(R.id.AllTripsList);

        // Setting the LayoutManager.
        mTripsRecyclerView.setLayoutManager(lm);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trips, container, false);

        FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MainActivity.pager.setCurrentItem(0, true);
            }
        });

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

    //Implementation of the tripsloader(s)
    @Override
    public Loader<List<Trip>> onCreateLoader(int id, Bundle bundle)
    {
        switch (id)
        {
            case TRIPS_SAVED_LOADER_ID:
                return new TripsLoader(getActivity(), getResources().getString(R.string.API_Trips));
            case TRIPS_REQUESTS_LOADER_ID:
                return new SpecialTripsLoader(getActivity(), getResources().getString(R.string.API_Trips_Requests));
            case TRIPS_REQUESTED_LOADER_ID:
                return new SpecialTripsLoader(getActivity(), getResources().getString(R.string.API_Trips_Requested));
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Trip>> loader, List<Trip> trips)
    {

        if(loader.getId()==TRIPS_SAVED_LOADER_ID)
        {
            if(mSavedTrips!=null)
            {
                mSavedTrips = trips;
                mAllTrips.addAll(mSavedTrips);
            }
        }
        else if(loader.getId()==TRIPS_REQUESTS_LOADER_ID)
        {
            for(Trip trip: trips){trip.setType("Ritaanvragen");}
            if(mRequestTrips!=null)
            {
                mRequestTrips = trips;
                mAllTrips.addAll(mRequestTrips);
            }
        }
        else if(loader.getId()==TRIPS_REQUESTED_LOADER_ID)
        {
            for(Trip trip: trips){trip.setType("Ritverzoeken");}
            if(mRequestedTrips!=null)
            {
                mRequestedTrips = trips;
                mAllTrips.addAll(mRequestedTrips);
            }
        }

        //Set adapter when all 3 loaders are ready
        if(loaderManager.getLoader(TRIPS_SAVED_LOADER_ID).isStarted()
                &&loaderManager.getLoader(TRIPS_REQUESTS_LOADER_ID).isStarted()
                &&loaderManager.getLoader(TRIPS_REQUESTED_LOADER_ID).isStarted())
        {
            //Check if list is not empty
            if(mAllTrips!=null) {
                if (!mAllTrips.isEmpty()) {
                    //Hide textview
                    TextView txbNoTrips = (TextView) getActivity().findViewById(R.id.txbNoTrips);
                    txbNoTrips.setVisibility(View.INVISIBLE);

                    //Show list
                    mTripsRecyclerView.setVisibility(View.VISIBLE);
                }
            }

            // Setting the adapter.
            mTripRecyclerAdapter = new TripRecyclerAdapter(getActivity(), mAllTrips);
            mTripsRecyclerView.setAdapter(mTripRecyclerAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Trip>> cursorLoader)
    {
        mAllTrips.clear();
        mTripRecyclerAdapter.updateList(mAllTrips);
    }

}
