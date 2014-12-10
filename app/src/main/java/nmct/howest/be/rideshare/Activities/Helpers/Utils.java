package nmct.howest.be.rideshare.Activities.Helpers;

import android.util.JsonReader;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Preben on 10/12/2014.
 */
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
        String dateTime =sb.toString();

        return dateTime;
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
            try{reader.close();}catch(IOException e){}
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

}
