package nmct.howest.be.rideshare.Activities.Helpers;

import android.content.Context;
import android.net.ConnectivityManager;

public class ConnectivityHelper {

    //Check internet connectivity
    public static boolean isNetworkAvailable(Context context)
    {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}
