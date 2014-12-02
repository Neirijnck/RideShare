package nmct.howest.be.rideshare.Activities.Helpers;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class APIHelper {

    public static void AddUser(String userName, String firstName, String lastName, String email, String fbToken, String fbLink, String fbID, String location, String gender, String regID) {
        try {
            HttpPost httppost = new HttpPost("http://188.226.154.228:8080/api/v1/profile");
            httppost.addHeader("auth", fbToken);

            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("userName", userName));
            parameters.add(new BasicNameValuePair("firstName", firstName));
            parameters.add(new BasicNameValuePair("lastName", lastName));
            parameters.add(new BasicNameValuePair("email", email));
            parameters.add(new BasicNameValuePair("fbLink", fbLink));
            parameters.add(new BasicNameValuePair("fbID", fbID));
            parameters.add(new BasicNameValuePair("location", location));
            parameters.add(new BasicNameValuePair("gender", gender));
            parameters.add(new BasicNameValuePair("notifications", regID));
            httppost.setEntity(new UrlEncodedFormEntity(parameters));

            PostAsync task = new PostAsync();
            task.execute(httppost);
        }
        catch (IOException e) {
            Log.d("", "Error in http connection " + e.toString());
        }
    }

    public static void EditUser(String userName, String firstName, String lastName, String fbToken, String location, String carType, String amountOfSeats)
    {
        try {
            HttpPut httpput = new HttpPut("http://188.226.154.228:8080/api/v1/profile");
            httpput.addHeader("auth", fbToken);


            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("userName", userName));
            parameters.add(new BasicNameValuePair("firstName", firstName));
            parameters.add(new BasicNameValuePair("lastName", lastName));
            parameters.add(new BasicNameValuePair("location", location));
            parameters.add(new BasicNameValuePair("carType", carType));
            parameters.add(new BasicNameValuePair("amountOfSeats", amountOfSeats));
            httpput.setEntity(new UrlEncodedFormEntity(parameters));

            PutAsync task = new PutAsync();
            task.execute(httpput);
        }
        catch (IOException e) {
            Log.d("", "Error in http connection " + e.toString());
        }
    }

    public static void PlanTrip(String from, String to, String date, String time, String price) {
        try {
            String datetime = "";

            //prim.trim("€");

            HttpPost httppost = new HttpPost("http://188.226.154.228:8080/api/v1/trips");
            httppost.addHeader("auth", "000");

            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("from", from));
            parameters.add(new BasicNameValuePair("to", to));

            if(!datetime.isEmpty())
                parameters.add(new BasicNameValuePair("datetime", datetime));

            if(!price.isEmpty())
                parameters.add(new BasicNameValuePair("price", price));

            httppost.setEntity(new UrlEncodedFormEntity(parameters));

            PostAsync task = new PostAsync();
            task.execute(httppost);
        }
        catch (IOException e) {
            Log.d("", "Error in http connection " + e.toString());
        }
    }


    //Helper Post
    public static class PostAsync extends AsyncTask<HttpPost, Void, String> {

        @Override
        protected String doInBackground(HttpPost... param) {
            try {
                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = param[0];
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                String result = convertStreamToString(entity.getContent());

                return new String(result);

            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    //Helper Put
    public static class PutAsync extends AsyncTask<HttpPut, Void, String>
    {
        @Override
        protected String doInBackground(HttpPut... param) {
            try
            {
                HttpClient httpClient = new DefaultHttpClient();

                HttpPut httpput = param[0];
                HttpResponse response = httpClient.execute(httpput);

                HttpEntity entity = response.getEntity();
                String result = convertStreamToString(entity.getContent());

                return new String(result);
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
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
