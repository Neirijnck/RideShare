package nmct.howest.be.rideshare.Helpers;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.ProfilePictureView;

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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import nmct.howest.be.rideshare.Models.Message;
import nmct.howest.be.rideshare.Models.User;
import nmct.howest.be.rideshare.R;
import nmct.howest.be.rideshare.RideshareApp;

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

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.getDefault());
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

    public static String parseNowToISOString() {
        Date date = Calendar.getInstance().getTime();
        String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .format(date);
        return formatted.substring(0, 22) + ":" + formatted.substring(22);
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

        String urlJsonUser = RideshareApp.getAppContext().getResources().getString(R.string.API_Profile);
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

    public static String getFacebookImgFromUserID(String token, String userID) throws IOException
    {
        String facebookImgUrl="";

        String urlJsonUser = RideshareApp.getAppContext().getResources().getString(R.string.API_Profile);
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
                    if (key.equals("facebookImg"))
                    {
                        facebookImgUrl = reader.nextString();
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
        return facebookImgUrl;
    }

    public static String getUserFacebookIDFromUserID(String token, String userID) throws IOException
    {
        String facebookID="";

        String urlJsonUser = RideshareApp.getAppContext().getResources().getString(R.string.API_Profile);
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

    public static void sendMessage(String token, EditText txtMessage, String matchID, String tripID) {
        String messageText = txtMessage.getText().toString().trim();
        if(TextUtils.isEmpty(messageText))
        {
            Toast.makeText(RideshareApp.getAppContext(), "Vul een bericht in", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Message m = new Message();
            m.setText(messageText);
            m.setDatetime(Utils.parseNowToISOString());
            APIHelper.AddMessageToMatch(token, matchID, m, tripID);
        }
    }

    public static void populateTravelers(LayoutInflater inflater, ViewGroup travelerContainer, List<User> travelerList)
    {
        if(travelerContainer==null)
        {
            //Nothing we can do, view already gone
            Log.e("Populate travelers", "Container reference gone");
        }

        travelerContainer.removeAllViews();

        for(User user : travelerList)
        {
            View travelerView = createTravelerView(inflater, travelerContainer, user.getFacebookID(), user.getUserName());
            travelerContainer.addView(travelerView);
        }
    }

    public static void populateMessages(LayoutInflater inflater, ViewGroup messagesContainer, List<Message> messageList, String myUserID)
    {
        if(messagesContainer==null)
        {
            //Nothing we can do, view already gone
            Log.e("Populate Messages", "Container reference gone");
        }

        messagesContainer.removeAllViews();

        for(Message message : messageList)
        {
            View messageView = createMessageView(inflater, messagesContainer, message.getText(), message.getDatetime(), message.getUserID(), myUserID);
            messagesContainer.addView(messageView);
        }
    }

    public static View createTravelerView(LayoutInflater inflater, ViewGroup travelerContainer, String facebookID, String userName)
    {
        View travelerView = inflater.inflate(R.layout.item_traveler, travelerContainer, false);

        ProfilePictureView imgTraveler = (ProfilePictureView) travelerView.findViewById(R.id.imgTraveler);
        imgTraveler.setProfileId(facebookID);

        TextView txtTravelerName = (TextView) travelerView.findViewById(R.id.txtTravelerName);
        txtTravelerName.setText(userName);

        return travelerView;
    }

    public static View createMessageView(LayoutInflater inflater, ViewGroup messagesContainer, String messageText, String messageDate, String messageUserID, String myUserID)
    {
        View messageView;
        if(messageUserID.equals(myUserID)) {
            messageView = inflater.inflate(R.layout.item_message_own, messagesContainer, false);
        }
        else
        {
            messageView = inflater.inflate(R.layout.item_message, messagesContainer, false);
        }

        TextView txtTextMessage = (TextView) messageView.findViewById(R.id.txbMessageText);
        txtTextMessage.setText(messageText);

        TextView txtDateMessage = (TextView) messageView.findViewById(R.id.txbMessageDate);
        txtDateMessage.setText(Utils.parseISOStringToDate(messageDate));

        return messageView;
    }

}
