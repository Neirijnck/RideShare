package nmct.howest.be.rideshare;

import android.app.Application;
import android.content.Context;

/**
 * Created by Preben on 5/12/2014.
 */
public class RideshareApp extends Application
{
    private static Context context;

    public void onCreate(){
        super.onCreate();
        this.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return RideshareApp.context;
    }
}
