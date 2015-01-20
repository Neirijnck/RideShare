package nmct.howest.be.rideshare.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import nmct.howest.be.rideshare.Helpers.Utils;
import nmct.howest.be.rideshare.Loaders.Tasks.SearchResultsTask;
import nmct.howest.be.rideshare.R;
import nmct.howest.be.rideshare.RideshareApp;

public class SearchResultsFragment extends Fragment
{
    //Variables
    private ProgressBar mProgressBar;
    private RecyclerView lstSearchResults;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mTxtNoResults;

    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(RideshareApp.getAppContext());
    String token = pref.getString("accessToken", "");

    public SearchResultsFragment() {}

    //New instance of this fragment with parameters
    public static SearchResultsFragment newInstance(String from, String fromPlaceid, String to,String toPlaceid, String date, String time, boolean share) {
        SearchResultsFragment fragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        args.putString("from", from);
        args.putString("to", to);
        args.putString("date", date);
        args.putString("time", time);
        args.putBoolean("share", share);
        args.putString("fromPlaceid", fromPlaceid);
        args.putString("toPlaceid", toPlaceid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_results, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarSearch);
        lstSearchResults = (RecyclerView) view.findViewById(R.id.lstSearchResults);
        mTxtNoResults = (TextView) view.findViewById(R.id.txtNoSearchResults);

        // Setting the LayoutManager.
        mLayoutManager = new LinearLayoutManager(RideshareApp.getAppContext(), LinearLayoutManager.VERTICAL, false);
        lstSearchResults.setLayoutManager(mLayoutManager);

        Bundle b = getArguments();

        //Has internet?
        Context c = getActivity();
        if(!Utils.isNetworkAvailable(c)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(c);
            builder.setTitle(getActivity().getResources().getString(R.string.dialogTitleNoConnection));
            builder.setMessage(getActivity().getResources().getString(R.string.dialogNoConnection));
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().finish();
                }
            });
            builder.show();
        }
        else {
            SearchResultsTask task = new SearchResultsTask(token, mProgressBar, lstSearchResults, mTxtNoResults);
            task.execute(b);
        }
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
