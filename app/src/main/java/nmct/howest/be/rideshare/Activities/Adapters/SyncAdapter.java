package nmct.howest.be.rideshare.Activities.Adapters;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import nmct.howest.be.rideshare.Activities.Helpers.SyncUtils;

/**
 * Created by Preben on 7/12/2014.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter
{
    ContentResolver mContentResolver;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
    {
        try {
            //Get accesstoken from sharedPreferences
            String accessToken = "";

            SyncUtils utils = new SyncUtils(getContext());
            utils.syncUser(accessToken, provider);
            utils.syncTrips(accessToken, provider);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
