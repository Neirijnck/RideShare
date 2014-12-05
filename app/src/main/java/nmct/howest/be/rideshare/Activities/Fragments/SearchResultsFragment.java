package nmct.howest.be.rideshare.Activities.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import nmct.howest.be.rideshare.Activities.Loaders.Tasks.SearchResultsTask;
import nmct.howest.be.rideshare.R;

/**
 * Created by Preben on 4/12/2014.
 */
public class SearchResultsFragment extends Fragment
{
    private ProgressBar mProgressBar;

    public SearchResultsFragment() { }

    //New instance of this fragment with parameters
    public static SearchResultsFragment newInstance(String from, String to) {
        SearchResultsFragment fragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        args.putString("from", from);
        args.putString("to", to);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_results, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarSearch);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //new SearchResultsTask(mProgressBar);

    }

}
