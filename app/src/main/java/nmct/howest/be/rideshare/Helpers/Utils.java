package nmct.howest.be.rideshare.Helpers;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.JsonReader;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utils
{
    public static boolean areAllFalse(boolean[] array)
    {
        for(boolean b : array) if(b) return false;
        return true;
    }

    public static String parseDateToISOString(String date, String time)
    {
        String inputPattern = "dd-MM-yyyy";
        String  outputPattern = "yyyy-MM-dd";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date dateObject = null;
        String str = null;

        try {
            dateObject = inputFormat.parse(date);
            str = outputFormat.format(dateObject);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        StringBuilder sb = new StringBuilder().append(str).append("T").append(time).append(":00.000Z");

        return sb.toString();
    }

    public static String ParseJsonStatusCode(String json)
    {
        String statusCode="";
        String message="";

        Reader stringReader = new StringReader(json);
        JsonReader reader = new JsonReader(stringReader);
        try
        {
            reader.beginObject();
            while (reader.hasNext())
            {
                while (reader.hasNext())
                {
                    String key = reader.nextName();
                    if (key.equals("status")) {
                        statusCode = reader.nextString();
                    } else if (key.equals("message")) {
                        message = reader.nextString();
                    }
                    else{
                        reader.skipValue();
                    }
                }
            }
            reader.endObject();
        }
        catch(IOException ex)
        {
            Log.e("IOException", ex.getMessage());
        }
        finally
        {
            try{reader.close();}catch(IOException e){
                Log.e("IOException", e.getMessage());
            }
        }

        return statusCode;
    }


    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static String parseBoolArrayToDays(boolean[] array)
    {
        String repeatString="";
        for(int i = 0; i < 7; i++)
        {
            if(array[i])
            {
                //Add day to full string
                switch(i)
                {
                    case 0:
                        repeatString += "ma, ";
                        break;
                    case 1:
                        repeatString += "di, ";
                        break;
                    case 2:
                        repeatString += "woe, ";
                        break;
                    case 3:
                        repeatString += "don, ";
                        break;
                    case 4:
                        repeatString += "vrij, ";
                        break;
                    case 5:
                        repeatString += "za, ";
                        break;
                    case 6:
                        repeatString += "zon, ";
                        break;
                }
            }
        }

        if(repeatString.isEmpty()) {
            repeatString = "Nooit";
        }
        else {
            repeatString = repeatString.substring(0, repeatString.length() - 2);
        }

        return repeatString;
    }

    public static String parseISOStringToDate(String ISODate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getDefault());
        String dateString="";
        try {
            Date date = sdf.parse(ISODate);

            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
            dateString = fmtOut.format(date);
        }catch (ParseException ex){
            Log.e("ParseException Date", ex.getMessage());}
        return dateString;
    }

    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager() .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static String getUserNameFromUserID(String token, String userID) throws IOException
    {
        String userName="";

        String urlJsonUser = "http://188.226.154.228:8080/api/v1/profile/";
        urlJsonUser += userID;

        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(urlJsonUser);

        httpGet.addHeader("Authorization", token);
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream in = entity.getContent();

        String JSONbody = Utils.convertStreamToString(in);
        Reader stringReader = new StringReader(JSONbody);

        JsonReader reader = new JsonReader(stringReader);
        try
        {
            reader.beginObject();
            while (reader.hasNext())
            {
                while (reader.hasNext()) {
                    String key = reader.nextName();
                    if (key.equals("userName"))
                    {
                        userName = reader.nextString();
                    }
                    else
                    {
                        reader.skipValue();
                    }
                }
            }
        }
        catch(IOException e)
        {
            Log.e("IOException", e.getMessage());
        }finally
        {
            try{reader.close();}catch(IOException e){Log.e("IOException", e.getMessage());}
            try{in.close();}catch(IOException e){Log.e("IOException", e.getMessage());}
        }
        return userName;
    }

    public static String getUserFacebookIDFromUserID(String token, String userID) throws IOException
    {
        String facebookID="";

        String urlJsonUser = "http://188.226.154.228:8080/api/v1/profile/";
        urlJsonUser += userID;

        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(urlJsonUser);

        httpGet.addHeader("Authorization", token);
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream in = entity.getContent();

        String JSONbody = Utils.convertStreamToString(in);
        Reader stringReader = new StringReader(JSONbody);

        JsonReader reader = new JsonReader(stringReader);
        try
        {
            reader.beginObject();
            while (reader.hasNext())
            {
                while (reader.hasNext()) {
                    String key = reader.nextName();
                    if (key.equals("facebookID"))
                    {
                        facebookID = reader.nextString();
                    }
                    else
                    {
                        reader.skipValue();
                    }
                }
            }
        }
        catch(IOException e)
        {
            Log.e("IOException", e.getMessage());
        }finally
        {
            try{reader.close();}catch(IOException e){ Log.e("IOException", e.getMessage()); }
            try{in.close();}catch(IOException e){ Log.e("IOException", e.getMessage()); }
        }

        return facebookID;
    }

    public static Bitmap getBitmapFromUrl(String url)
    {
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try
        {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            bm = BitmapFactory.decodeStream(bis);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (bis != null)
            {
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return bm;
    }

    public static String parseISOStringToTime(String ISODate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String timeString = "";
        try {
            Date date = sdf.parse(ISODate);
            long millis = date.getTime();

            //long second = (millis / 1000) % 60;
            long minute = (millis / (1000 * 60)) % 60;
            long hour = (millis / (1000 * 60 * 60)) % 24;

            timeString = String.format("%02d:%02d", hour, minute);
        }
        catch (ParseException ex){
            Log.e("ParseException Date", ex.getMessage());
        }
        return timeString;
    }

    public static String setPayment(String Payment)
    {
        Log.d("Payment", Payment);

        if(Payment.isEmpty()) {
            return "Geen bedrag opgegeven.";
        }
        else {
            return "€" + Payment;
        }
    }

    public static String convertStatus(int status)
    {
        switch(status)
        {
            case 0:
                return "Nog niet geaccepteerd";
            case 1:
                return "Geaccepteerd";
            case 2:
                return "Geweigerd";
            default:
                return "";
        }
    }

    public static int convertStatusToProgress(int status)
    {
        switch (status)
        {
            case 0:
                return 1 ;
            case 1:
                return 100;
            case 2:
                return 100;
            default:
                return 0;
        }
    }

}
