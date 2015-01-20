package nmct.howest.be.rideshare;

import android.app.Application;
import android.content.Context;

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
