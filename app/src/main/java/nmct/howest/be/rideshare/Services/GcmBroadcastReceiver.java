package nmct.howest.be.rideshare.Services;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();
        String data = extras.getString("data");

        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),GcmIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);

        /*
        Intent msgrcv = new Intent("Msg");
        msgrcv.putExtra("msg", extras.getString("msg"));
        msgrcv.putExtra("fromu", extras.getString("fromu"));
        msgrcv.putExtra("fromname", extras.getString("name"));

        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);*/
    }

}