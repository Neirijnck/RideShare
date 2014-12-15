package nmct.howest.be.rideshare.Activities.Helpers;

import android.os.AsyncTask;
import android.text.TextUtils;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nmct.howest.be.rideshare.RideshareApp;

public class APIHelper {

    public static void AddUser(String userName, String firstName, String lastName, String email, String fbToken, String fbLink, String fbID, String location, String gender, String regID, String fbImg, String birthday) {
        try {
            HttpPost httppost = new HttpPost("http://188.226.154.228:8080/api/v1/profile");

            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("userName", userName));
            parameters.add(new BasicNameValuePair("firstName", firstName));
            parameters.add(new BasicNameValuePair("lastName", lastName));
            parameters.add(new BasicNameValuePair("email", email));
            parameters.add(new BasicNameValuePair("facebookToken", fbToken));
            parameters.add(new BasicNameValuePair("facebookLink", fbLink));
            parameters.add(new BasicNameValuePair("facebookID", fbID));
            parameters.add(new BasicNameValuePair("location", location));
            parameters.add(new BasicNameValuePair("gender", gender));
            parameters.add(new BasicNameValuePair("registrationIDs", "666"));
            parameters.add(new BasicNameValuePair("facebookImg", fbImg));
            parameters.add(new BasicNameValuePair("birthday", birthday));
            httppost.setEntity(new UrlEncodedFormEntity(parameters));

            PostAsync task = new PostAsync();
            task.execute(httppost);
        }
        catch (IOException e) {
            Log.d("", "Error in http connection " + e.toString());
        }
    }

    //Minimum parameters
    public static void EditUser(String userName, String firstName, String lastName, String fbToken, String location)
    {
        try {
            HttpPut httpput = new HttpPut("http://188.226.154.228:8080/api/v1/profile");
            httpput.addHeader("Authorization", fbToken);


            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("userName", userName));
            parameters.add(new BasicNameValuePair("firstName", firstName));
            parameters.add(new BasicNameValuePair("lastName", lastName));
            parameters.add(new BasicNameValuePair("location", location));
            parameters.add(new BasicNameValuePair("carType", ""));
            parameters.add(new BasicNameValuePair("amountOfSeats", ""));
            httpput.setEntity(new UrlEncodedFormEntity(parameters));

            PutAsync task = new PutAsync();
            task.execute(httpput);
        }
        catch (IOException e) {
            Log.d("", "Error in http connection " + e.toString());
        }
    }

    public static void EditUser(String userName, String firstName, String lastName, String fbToken, String location, String carType)
    {
        try {
            HttpPut httpput = new HttpPut("http://188.226.154.228:8080/api/v1/profile");
            httpput.addHeader("Authorization", fbToken);


            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("userName", userName));
            parameters.add(new BasicNameValuePair("firstName", firstName));
            parameters.add(new BasicNameValuePair("lastName", lastName));
            parameters.add(new BasicNameValuePair("location", location));
            parameters.add(new BasicNameValuePair("carType", carType));
            parameters.add(new BasicNameValuePair("amountOfSeats", ""));
            httpput.setEntity(new UrlEncodedFormEntity(parameters));

            PutAsync task = new PutAsync();
            task.execute(httpput);
        }
        catch (IOException e) {
            Log.d("", "Error in http connection " + e.toString());
        }
    }

    public static void EditUser(String userName, String firstName, String lastName, String fbToken, String location, int amountOfSeats)
    {
        try {
            HttpPut httpput = new HttpPut("http://188.226.154.228:8080/api/v1/profile");
            httpput.addHeader("Authorization", fbToken);


            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("userName", userName));
            parameters.add(new BasicNameValuePair("firstName", firstName));
            parameters.add(new BasicNameValuePair("lastName", lastName));
            parameters.add(new BasicNameValuePair("location", location));

            String amount = Integer.toString(amountOfSeats);
            parameters.add(new BasicNameValuePair("amountOfSeats", amount));
            parameters.add(new BasicNameValuePair("carType", ""));
            httpput.setEntity(new UrlEncodedFormEntity(parameters));

            PutAsync task = new PutAsync();
            task.execute(httpput);
        }
        catch (IOException e) {
            Log.d("", "Error in http connection " + e.toString());
        }
    }

    public static void EditUser(String userName, String firstName, String lastName, String fbToken, String location, String carType, String amountOfSeats)
    {
        try {
            HttpPut httpput = new HttpPut("http://188.226.154.228:8080/api/v1/profile");
            httpput.addHeader("Authorization", fbToken);


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

    //Minimum parameters
    public static void PlanTrip(String fbToken, String from, String to, String date, String time)
    {
        try {
            String dateTime = Utils.parseDateToISOString(date, time);

            HttpPost httppost = new HttpPost("http://188.226.154.228:8080/api/v1/trips");
            httppost.addHeader("Authorization", fbToken);

            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("from", from));
            parameters.add(new BasicNameValuePair("to", to));

            if(!dateTime.isEmpty())
                parameters.add(new BasicNameValuePair("datetime", dateTime));

            httppost.setEntity(new UrlEncodedFormEntity(parameters));

            PostAsync task = new PostAsync();
            task.execute(httppost);
        }
        catch (IOException e) {
            Log.d("", "Error in http connection " + e.toString());
        }
    }

    //With price
    public static void PlanTrip(String fbToken, String from, String to, String date, String time, String price) {
        try {
            String dateTime = Utils.parseDateToISOString(date, time);

            price = TextUtils.substring(price, 1, price.length());

            HttpPost httppost = new HttpPost("http://188.226.154.228:8080/api/v1/trips");
            httppost.addHeader("Authorization", fbToken);

            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("from", from));
            parameters.add(new BasicNameValuePair("to", to));

            if(!dateTime.isEmpty())
                parameters.add(new BasicNameValuePair("datetime", dateTime));

            if(!price.isEmpty())
                parameters.add(new BasicNameValuePair("payment", price));

            httppost.setEntity(new UrlEncodedFormEntity(parameters));

            PostAsync task = new PostAsync();
            task.execute(httppost);
        }
        catch (IOException e) {
            Log.d("", "Error in http connection " + e.toString());
        }
    }

    //With repeat
    public static void PlanTrip(String fbToken, String from, String to, String date, String time, boolean[] repeat)
    {
        try {
            String dateTime = Utils.parseDateToISOString(date, time);


            HttpPost httppost = new HttpPost("http://188.226.154.228:8080/api/v1/trips");
            httppost.addHeader("Authorization", fbToken);

            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("from", from));
            parameters.add(new BasicNameValuePair("to", to));

            if(!dateTime.isEmpty())
                parameters.add(new BasicNameValuePair("datetime", dateTime));

            parameters.add(new BasicNameValuePair("repeat.mo", ""+repeat[0]));
            parameters.add(new BasicNameValuePair("repeat.tu", ""+repeat[1]));
            parameters.add(new BasicNameValuePair("repeat.we", ""+repeat[2]));
            parameters.add(new BasicNameValuePair("repeat.th", ""+repeat[3]));
            parameters.add(new BasicNameValuePair("repeat.fr", ""+repeat[4]));
            parameters.add(new BasicNameValuePair("repeat.sa", ""+repeat[5]));
            parameters.add(new BasicNameValuePair("repeat.su", ""+repeat[6]));

            httppost.setEntity(new UrlEncodedFormEntity(parameters));

            PostAsync task = new PostAsync();
            task.execute(httppost);
        }
        catch (IOException e) {
            Log.d("", "Error in http connection " + e.toString());
        }
    }

    //With everything filled in
    public static void PlanTrip(String fbToken, String from, String to, String date, String time, String price, boolean[] repeat)
    {
        try {
            String dateTime = Utils.parseDateToISOString(date, time);

            price = TextUtils.substring(price, 1, price.length());

            HttpPost httppost = new HttpPost("http://188.226.154.228:8080/api/v1/trips");
            httppost.addHeader("Authorization", fbToken);

            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("from", from));
            parameters.add(new BasicNameValuePair("to", to));

            if(!dateTime.isEmpty())
                parameters.add(new BasicNameValuePair("datetime", dateTime));

            if(!price.isEmpty())
                parameters.add(new BasicNameValuePair("payment", price));

            parameters.add(new BasicNameValuePair("repeat.mo", ""+repeat[0]));
            parameters.add(new BasicNameValuePair("repeat.tu", ""+repeat[1]));
            parameters.add(new BasicNameValuePair("repeat.we", ""+repeat[2]));
            parameters.add(new BasicNameValuePair("repeat.th", ""+repeat[3]));
            parameters.add(new BasicNameValuePair("repeat.fr", ""+repeat[4]));
            parameters.add(new BasicNameValuePair("repeat.sa", ""+repeat[5]));
            parameters.add(new BasicNameValuePair("repeat.su", ""+repeat[6]));

            httppost.setEntity(new UrlEncodedFormEntity(parameters));

            PostAsync task = new PostAsync();
            task.execute(httppost);
        }
        catch (IOException e) {
            Log.d("", "Error in http connection " + e.toString());
        }
    }


    //Helper Post
    public static class PostAsync extends AsyncTask<HttpPost, Void, String>
    {

        @Override
        protected String doInBackground(HttpPost... param) {
            try {
                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = param[0];
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                String result = Utils.convertStreamToString(entity.getContent());


                return new String(result);

            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            //Parse json for status code
            //if 200 = Toast for success
            String statusCode = Utils.ParseJsonStatusCode(result);
            if(statusCode.equals("200")||statusCode.equals("201"))
            {
                Toast.makeText(RideshareApp.getAppContext(), "Succesvol opgeslagen in database", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(RideshareApp.getAppContext(), "Er is iets misgelopen", Toast.LENGTH_SHORT).show();
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
                String result = Utils.convertStreamToString(entity.getContent());

                return new String(result);
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            //Parse json for status code
            //if 200 = Toast for success
            String statusCode = Utils.ParseJsonStatusCode(result);
            if(statusCode.equals("200")||statusCode.equals("201"))
            {
                Toast.makeText(RideshareApp.getAppContext(), "Succesvol aangepast in database", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(RideshareApp.getAppContext(), "Er is iets misgelopen", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
