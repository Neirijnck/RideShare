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
import java.util.TimeZone;

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

    public static String parseBoolArrayToDays(boolean[] array)
    {
        String repeatString="";
        for(int i = 0; i < 7; i++)
        {
            if(array[i]==true)
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
        repeatString = repeatString.substring(0, repeatString.length()-2);
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

}
