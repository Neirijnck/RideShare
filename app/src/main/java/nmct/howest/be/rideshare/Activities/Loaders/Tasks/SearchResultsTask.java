package nmct.howest.be.rideshare.Activities.Loaders.Tasks;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by Preben on 4/12/2014.
 */
public class SearchResultsTask extends AsyncTask<Void, Integer, Void>
{
    private final ProgressBar mProgress;

    public SearchResultsTask(final ProgressBar progress)
    {
        this.mProgress = progress;
    }

    @Override
    protected Void doInBackground(Void... params)
    {
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        //Hide the progressbar after loading
        mProgress.setVisibility(View.INVISIBLE);
    }
}
