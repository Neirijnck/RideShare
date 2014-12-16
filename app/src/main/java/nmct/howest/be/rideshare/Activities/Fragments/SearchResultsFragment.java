package nmct.howest.be.rideshare.Activities.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import nmct.howest.be.rideshare.Activities.Helpers.ConnectivityHelper;
import nmct.howest.be.rideshare.Activities.Loaders.Tasks.SearchResultsTask;
import nmct.howest.be.rideshare.R;
import nmct.howest.be.rideshare.RideshareApp;

/**
 * Created by Preben on 4/12/2014.
 */
public class SearchResultsFragment extends Fragment
{
    private ProgressBar mProgressBar;
    private LinearLayout lstSearchResults;
    private TextView mTxtNoResults;
    private ScrollView layout_search_results;

    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(RideshareApp.getAppContext());
    String token = pref.getString("accessToken", "");

    public SearchResultsFragment() { }

    //New instance of this fragment with parameters
    public static SearchResultsFragment newInstance(String from, String to, String date, String time, boolean share) {
        SearchResultsFragment fragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        args.putString("from", from);
        args.putString("to", to);
        args.putString("date", date);
        args.putString("time", time);
        args.putBoolean("share", share);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_results, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarSearch);
        lstSearchResults = (LinearLayout) view.findViewById(R.id.lstSearchResults);
        mTxtNoResults = (TextView) view.findViewById(R.id.txtNoSearchResults);
        layout_search_results = (ScrollView) view.findViewById(R.id.layout_search_results);

        Bundle b = getArguments();

        //Has internet?
        Context c = getActivity();
        if(!ConnectivityHelper.isNetworkAvailable(c)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(c);
            builder.setTitle("No Internet connection.");
            builder.setMessage("You have no internet connection");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().finish();
                }
            });
            builder.show();
        }
        else {
            SearchResultsTask task = new SearchResultsTask(token, mProgressBar, lstSearchResults, mTxtNoResults, layout_search_results);
            task.execute(b);
        }

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
