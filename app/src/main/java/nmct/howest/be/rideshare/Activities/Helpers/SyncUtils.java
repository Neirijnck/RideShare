package nmct.howest.be.rideshare.Activities.Helpers;

import android.content.ContentProviderClient;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Preben on 7/12/2014.
 */
public class SyncUtils
{
    private final String mServer;
    private final Context mContext;

    public SyncUtils(Context ctx) {
        mContext = ctx;
        try {
            ApplicationInfo appInfo = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = appInfo.metaData;
            mServer = bundle.getString("nmct.howest.be.rideshare.webservice.auth.location");
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void syncTrips(String accessToken, ContentProviderClient contentProviderClient) throws Exception
    {
        try
        {
            URL url = new URL(mServer + "/api/v1/trips");

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Authorization", accessToken);
            connection.setUseCaches (false);



        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void syncUser(String accessToken, ContentProviderClient contentProviderClient) throws Exception
    {
        try
        {
            URL url = new URL(mServer + "/api/v1/profile");

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Authorization", accessToken);
            connection.setUseCaches (false);



        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
