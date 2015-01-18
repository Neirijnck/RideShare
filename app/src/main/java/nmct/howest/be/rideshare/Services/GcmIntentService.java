package nmct.howest.be.rideshare.Services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import nmct.howest.be.rideshare.Activities.MainActivity;
import nmct.howest.be.rideshare.R;

public class GcmIntentService extends IntentService {

    SharedPreferences prefs;
    NotificationCompat.Builder notification;
    NotificationManager manager;

    public GcmIntentService() {
        super("GcmIntentService");
        //super("451680909206");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // At this point SimpleWakefulReceiver is still holding a wake lock
        // for us.  We can do whatever we need to here and then tell it that
        // it can release the wakelock.  This sample just does some slow work,
        // but more complicated implementations could take their own wake
        // lock here before releasing the receiver's.
        //
        // Note that when using this approach you should be aware that if your
        // service gets killed and restarted while in the middle of such work
        // (so the Intent gets re-delivered to perform the work again), it will
        // at that point no longer be holding a wake lock since we are depending
        // on SimpleWakefulReceiver to that for us.  If this is a concern, you can
        // acquire a separate wake lock here.

        /*for (int i=0; i<5; i++) {
            Log.i("SimpleWakefulReceiver", "Running service " + (i+1)
                    + "/5 @ " + SystemClock.elapsedRealtime());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {}
        }
        Log.i("SimpleWakefulReceiver", "Completed service @ " + SystemClock.elapsedRealtime());
        GcmBroadcastReceiver.completeWakefulIntent(intent);*/

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                    Log.e("Log","Error");

            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.e("Log","Error");

            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                sendNotification(extras.getString("Type", ""), extras.getString("UserName", ""));
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String type, String userName) {

        Intent intent = new Intent(this, MainActivity.class);
        String msg = "";

        switch (type) {
            case "Request":
                intent.putExtra("PAGE", 2);
                msg = userName + " " + getString(R.string.requestNotification) + ".";
                intent.putExtra("TOAST", msg);
                break;
            case "Review":
                intent.putExtra("PAGE", 3);
                msg = userName + " " + getString(R.string.reviewNotification) + ".";
                intent.putExtra("TOAST", msg);
                break;
            case "Accept":
                intent.putExtra("PAGE", 2);
                msg = getString(R.string.acceptedNotification) + " " + userName + ".";
                intent.putExtra("TOAST", msg);
                break;
            case "Message":
                intent.putExtra("PAGE", 2);
                msg = userName + " " + getString(R.string.messageNotification) + ".";
                intent.putExtra("TOAST", msg);
                break;
        }

        PendingIntent contentIntent = PendingIntent.getActivity(this, 1000,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        notification = new NotificationCompat.Builder(this);
        notification.setContentTitle(getString(R.string.app_name));
        notification.setContentText(msg);
        notification.setSmallIcon(R.drawable.ic_stat_app_notification_icon);
        notification.setContentIntent(contentIntent);
        notification.setAutoCancel(true);

        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification.build());
    }

}