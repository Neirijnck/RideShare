package nmct.howest.be.rideshare.Adapters;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.os.Bundle;
import android.preference.PreferenceManager;

import nmct.howest.be.rideshare.Helpers.SyncUtils;
import nmct.howest.be.rideshare.RideshareApp;

public class SyncAdapter extends AbstractThreadedSyncAdapter
{
    //Variables
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
            //Get access token from sharedPreferences
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(RideshareApp.getAppContext());
            String token = pref.getString("accessToken", "");

            SyncUtils utils = new SyncUtils(getContext());
            utils.syncUser(token, provider);
//            utils.syncReviews(token, provider);
//            utils.syncTrips(token, provider);
//            utils.syncMatches(token, provider);
//            utils.syncMessages(token, provider);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
