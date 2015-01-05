package nmct.howest.be.rideshare.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import nmct.howest.be.rideshare.Activities.DetailsActivity;
import nmct.howest.be.rideshare.Activities.SearchActivity;
import nmct.howest.be.rideshare.Adapters.TripRequestAdapter;
import nmct.howest.be.rideshare.Adapters.TripRequestedAdapter;
import nmct.howest.be.rideshare.Adapters.TripSavedAdapter;
import nmct.howest.be.rideshare.Loaders.Json.SpecialTripsLoader;
import nmct.howest.be.rideshare.Loaders.Json.TripsLoader;
import nmct.howest.be.rideshare.Models.Match;
import nmct.howest.be.rideshare.Models.Trip;
import nmct.howest.be.rideshare.R;

public class TripsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Trip>>
{
    private LoaderManager loaderManager;
    private static final int TRIPS_SAVED_LOADER_ID = 0;
    private static final int TRIPS_REQUESTS_LOADER_ID = 10;
    private static final int TRIPS_REQUESTED_LOADER_ID = 20;

    private List<Trip> mTrips;
    private List<Trip> mTripsRequests;
    private List<Trip> mTripsRequested;

    private ArrayAdapter mAdapterTripSaved;
    private LinearLayout listMyTrips;

    private ArrayAdapter mAdapterTripRequest;
    private LinearLayout listRequestTrips;

    private ArrayAdapter mAdapterTripRequested;
    private LinearLayout listRequestedTrips;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trips, container, false);


        //Lists ophalen en opvullen
        listMyTrips = (LinearLayout) view.findViewById(R.id.lstOpgeslagenRitten);
        listRequestTrips = (LinearLayout) view.findViewById(R.id.lstRitAanvragen);
        listRequestedTrips = (LinearLayout) view.findViewById(R.id.lstRitVerzoeken);

        //Setting adapters
        mAdapterTripSaved = new TripSavedAdapter(getActivity(), R.layout.card_trip, R.id.txtTripInfo);
        mAdapterTripRequest = new TripRequestAdapter(getActivity(), R.layout.card_trip, R.id.txtTripInfo);
        mAdapterTripRequested = new TripRequestedAdapter(getActivity(), R.layout.card_trip, R.id.txtTripInfo);

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
            case 0:
                return new TripsLoader(getActivity(), getResources().getString(R.string.API_Trips));
            case 10:
                return new SpecialTripsLoader(getActivity(), getResources().getString(R.string.API_Trips_Requests));
            case 20:
                return new SpecialTripsLoader(getActivity(), getResources().getString(R.string.API_Trips_Requested));
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Trip>> loader, List<Trip> trips)
    {

        if(loader.getId()==0)
        {
            mTrips = trips;
            if(mTrips!=null)
            {
                mAdapterTripSaved.addAll(mTrips);
            }

            //Adding items to linear layouts
            int adaptercountSavedTrips = mAdapterTripSaved.getCount();
            for(int i =0; i < adaptercountSavedTrips; i++)
            {
                View item = mAdapterTripSaved.getView(i, null, null);

                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
                        Bundle b = new Bundle();
                        int pos = (int)v.getTag();
                        String id = mTrips.get(pos).getID();
                        b.putString("id", id);
                        b.putInt("type", 2);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });
                listMyTrips.addView(item);
            }
        }
        else if(loader.getId()==10)
        {
            mTripsRequests = trips;
            if(mTripsRequests!=null)
            {
                mAdapterTripRequest.addAll(mTripsRequests);
            }

            //Adding items to linear layouts
            int adaptercountRequests = mAdapterTripRequest.getCount();
            for(int i =0; i < adaptercountRequests; i++)
            {
                View item = mAdapterTripRequest.getView(i, null, null);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
                        Bundle b = new Bundle();
                        int pos = (int)v.getTag();
                        Trip trip = mTripsRequests.get(pos);
                        String id = trip.getID();
                        String matchID = "";
                        for(Match match : trip.getMatches()){matchID = match.getId();}

                        b.putString("id", id);
                        b.putInt("type", 1);
                        b.putString("matchID", matchID);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });
                listRequestTrips.addView(item);
            }
        }
        else if(loader.getId()==20)
        {
            mTripsRequested = trips;
            if(mTripsRequested!=null)
            {
                mAdapterTripRequested.addAll(mTripsRequested);
            }

            //Adding items to linear layouts
            int adaptercountRequested = mAdapterTripRequested.getCount();
            for(int i=0; i < adaptercountRequested; i++)
            {
                View item = mAdapterTripRequested.getView(i, null, null);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
                        Bundle b = new Bundle();
                        int pos = (int)v.getTag();
                        String id = mTripsRequested.get(pos).getID();
                        b.putString("id", id);
                        b.putInt("type", 0);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });
                listRequestedTrips.addView(item);
            }
        }

        //Check if lists are not empty
        if(mTrips!=null&&mTripsRequests!=null&&mTripsRequested!=null) {
            if (!mTrips.isEmpty() || !mTripsRequests.isEmpty() || !mTripsRequested.isEmpty()) {
                //Hide textview
                TextView txbNoTrips = (TextView) getActivity().findViewById(R.id.txbNoTrips);
                txbNoTrips.setVisibility(View.INVISIBLE);

                //Show lists
                ScrollView layoutTrips = (ScrollView) getActivity().findViewById(R.id.layout_trip_lists);
                layoutTrips.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Trip>> cursorLoader)
    {
        mTrips.clear();
        mAdapterTripSaved.notifyDataSetChanged();
        mAdapterTripSaved.clear();

        mTripsRequests.clear();
        mAdapterTripRequest.notifyDataSetChanged();

        mTripsRequested.clear();
        mAdapterTripRequested.notifyDataSetChanged();
    }

}
