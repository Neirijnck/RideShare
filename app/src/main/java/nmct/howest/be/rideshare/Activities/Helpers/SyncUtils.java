package nmct.howest.be.rideshare.Activities.Helpers;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.JsonReader;

import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import nmct.howest.be.rideshare.Activities.Loaders.Database.Contract;

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

            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            JsonReader reader = new JsonReader(isr);
            try
            {
                reader.beginArray();
                while(reader.hasNext()) {

                    ContentValues values = new ContentValues();

                    reader.beginObject();
                    while (reader.hasNext()) {

                        while (reader.hasNext()) {
                            String key = reader.nextName();
                            if (key.equals("_id"))
                            {
                                values.put(Contract.Trip._ID, reader.nextString());
                            }
                            else if (key.equals("userID"))
                            {
                                values.put(Contract.Trip.KEY_USERID, reader.nextString());
                            }
                            else if (key.equals("from"))
                            {
                                values.put(Contract.Trip.KEY_FROM, reader.nextString());
                            }
                            else if (key.equals("to")) {
                                values.put(Contract.Trip.KEY_TO, reader.nextString());
                            }
                            else if (key.equals("datetime")) {
                                values.put(Contract.Trip.KEY_DATE_TIME, reader.nextString());
                            }
                            else if (key.equals("payment"))
                            {
                                values.put(Contract.Trip.KEY_PAYMENT, reader.nextString());
                            }
                            else if (key.equals("repeat"))
                            {
                                boolean mo;
                                boolean tu;
                                boolean we;
                                boolean th;
                                boolean fr;
                                boolean sa;
                                boolean su;
                                boolean[] repeats = new boolean[7];
                                reader.beginObject();
                                while (reader.hasNext()) {
                                    String key_second = reader.nextName();
                                    if (key_second.equals("mo")) {
                                        mo = reader.nextBoolean();
                                        repeats[0] = mo;
                                    } else if (key_second.equals("tu")) {
                                        tu = reader.nextBoolean();
                                        repeats[1] = tu;
                                    } else if (key_second.equals("we")) {
                                        we = reader.nextBoolean();
                                        repeats[2] = we;
                                    } else if (key_second.equals("th")) {
                                        th = reader.nextBoolean();
                                        repeats[3] = th;
                                    } else if (key_second.equals("fr")) {
                                        fr = reader.nextBoolean();
                                        repeats[4] = fr;
                                    } else if (key_second.equals("sa")) {
                                        sa = reader.nextBoolean();
                                        repeats[5] = sa;
                                    } else if (key_second.equals("su")) {
                                        su = reader.nextBoolean();
                                        repeats[6] = su;
                                    } else {
                                        reader.skipValue();
                                    }
                                }
                                reader.endObject();
                                //trip.setRepeat(repeats);
                                //values.put(Contract.Trip.KEY_REPEAT, repeats);
                            }
                            else {
                                reader.skipValue();
                            }
                        }
                    }
                    reader.endObject();

                    Uri uri = Contract.Trip.ITEM_CONTENT_URI.buildUpon().appendEncodedPath(values.getAsString(Contract.Trip._ID)).build();
                    Cursor cursor = contentProviderClient.query(
                            uri,
                            new String[]{Contract.Trip._ID}, null, null, null);
                    if(cursor.getCount() > 0) {
                        values.remove(Contract.Trip._ID);
                        contentProviderClient.update(uri, values, null, null);
                    } else {
                        contentProviderClient.insert(Contract.Trip.CONTENT_URI, values);
                    }


                }
                reader.endArray();
                isr.close();
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void syncMatches(String accessToken, ContentProviderClient contentProviderClient) throws Exception
    {
        try {
            String userID = "";
            URL url = new URL(mServer + "/api/v1/trips/" + userID + "/match");

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Authorization", accessToken);
            connection.setUseCaches(false);

            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            JsonReader reader = new JsonReader(isr);
            try {
                reader.beginArray();
                while (reader.hasNext()) {

                    ContentValues values = new ContentValues();

                    reader.beginObject();
                    while (reader.hasNext())
                    {
                        String key = reader.nextName();
                        if (key.equals("userID")) {
                            values.put(Contract.Match.KEY_USERID, reader.nextString());
                        } else if (key.equals("from")) {
                            values.put(Contract.Match.KEY_FROM, reader.nextString());
                        } else if (key.equals("to")) {
                            values.put(Contract.Match.KEY_TO, reader.nextString());
                        } else if (key.equals("datetime")) {
                            values.put(Contract.Match.KEY_DATE_TIME, reader.nextString());
                        } else if (key.equals("status")) {
                            values.put(Contract.Match.KEY_STATUS, reader.nextInt());
                        } else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();

                    Uri uri = Contract.Match.ITEM_CONTENT_URI.buildUpon().appendEncodedPath(values.getAsString(Contract.Match._ID)).build();
                    Cursor cursor = contentProviderClient.query(
                            uri,
                            new String[]{Contract.Match._ID}, null, null, null);
                    if(cursor.getCount() > 0) {
                        values.remove(Contract.Match._ID);
                        contentProviderClient.update(uri, values, null, null);
                    } else {
                        contentProviderClient.insert(Contract.Match.CONTENT_URI, values);
                    }

                }
                reader.endArray();
                isr.close();
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
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

            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            JsonReader reader = new JsonReader(isr);
            try {
                ContentValues values = new ContentValues();
                reader.beginObject();
                while (reader.hasNext()) {

                    while (reader.hasNext()) {
                        String key = reader.nextName();
                        if (key.equals("_id"))
                        {
                            values.put(Contract.User._ID, reader.nextString());
                        }
                        else if (key.equals("userName"))
                        {
                            values.put(Contract.User.KEY_USERNAME, reader.nextString());
                        }
                        else if (key.equals("facebookLink"))
                        {
                            values.put(Contract.User.KEY_FACEBOOK_URL, reader.nextString());
                        }
                        else if(key.equals("facebookID"))
                        {
                            values.put(Contract.User.KEY_FACEBOOK_ID, reader.nextString());
                        }
                        else if (key.equals("firstName"))
                        {
                            values.put(Contract.User.KEY_FIRSTNAME, reader.nextString());
                        }
                        else if (key.equals("lastName"))
                        {
                            values.put(Contract.User.KEY_LASTNAME, reader.nextString());
                        }
                        else if (key.equals("email"))
                        {
                            values.put(Contract.User.KEY_EMAIL, reader.nextString());
                        }
                        else if (key.equals("gender"))
                        {
                            values.put(Contract.User.KEY_GENDER, reader.nextString());
                        }
                        else if (key.equals("birthday"))
                        {
                            values.put(Contract.User.KEY_BIRTHDAY, reader.nextString());
                        }
                        else if (key.equals("location"))
                        {
                            values.put(Contract.User.KEY_LOCATION, reader.nextString());
                        }
                        else if (key.equals("carType"))
                        {
                            values.put(Contract.User.KEY_CARTYPE, reader.nextString());
                        }
                        else if (key.equals("amountOfSeats"))
                        {
                            values.put(Contract.User.KEY_AMOUNT_OF_SEATS, reader.nextString());
                        }
                        else
                        {
                            reader.skipValue();
                        }
                    }
                }
                reader.endObject();

                Uri uri = Contract.User.ITEM_CONTENT_URI.buildUpon().appendEncodedPath(values.getAsString(Contract.User._ID)).build();
                Cursor cursor = contentProviderClient.query(
                        uri,
                        new String[]{Contract.User._ID}, null, null, null);
                if(cursor.getCount() > 0) {
                    values.remove(Contract.User._ID);
                    contentProviderClient.update(uri, values, null, null);
                } else {
                    contentProviderClient.insert(Contract.User.CONTENT_URI, values);
                }

                isr.close();
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void syncReviews(String accessToken, ContentProviderClient contentProviderClient) throws Exception {
        try {
            String userID = "";
            URL url = new URL(mServer + "/api/v1/profile/" + userID + "/review");

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Authorization", accessToken);
            connection.setUseCaches(false);

            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            JsonReader reader = new JsonReader(isr);

            reader.beginArray();
            while(reader.hasNext())
            {
                ContentValues values = new ContentValues();
                reader.beginObject();
                while(reader.hasNext())
                {
                    String key = reader.nextName();
                    if(key.equals("userID"))
                    {
                        values.put(Contract.Review.KEY_USERID, reader.nextString());
                    }
                    else if(key.equals("date"))
                    {
                        values.put(Contract.Review.KEY_CREATED_ON, reader.nextString());
                    }
                    else if(key.equals("score"))
                    {
                        values.put(Contract.Review.KEY_SCORE, reader.nextInt());
                    }
                    else if(key.equals("text"))
                    {
                        values.put(Contract.Review.KEY_TEXT, reader.nextString());
                    }
                    else{reader.skipValue();}
                }
                reader.endObject();

                Uri uri = Contract.Review.ITEM_CONTENT_URI.buildUpon().appendEncodedPath(values.getAsString(Contract.Review._ID)).build();
                Cursor cursor = contentProviderClient.query(
                        uri,
                        new String[]{Contract.Review._ID}, null, null, null);
                if(cursor.getCount() > 0) {
                    values.remove(Contract.Review._ID);
                    contentProviderClient.update(uri, values, null, null);
                } else {
                    contentProviderClient.insert(Contract.Review.CONTENT_URI, values);
                }

            }
            reader.endArray();

        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
