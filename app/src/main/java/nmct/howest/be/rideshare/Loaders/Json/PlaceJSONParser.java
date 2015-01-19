package nmct.howest.be.rideshare.Loaders.Json;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PlaceJSONParser {

    public static String KEY = "key=AIzaSyAHLPp0T90SHdIrRs0mbdLRNzMquuiLTdM";
    public static List<HashMap<String, String>> PLACES = new ArrayList<HashMap<String, String>>();

    //Download a URL
    public static String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }
            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading ", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }



    // Parse the Place API
    public List<HashMap<String,String>> parse(JSONObject jObject){
        JSONArray jPlaces = null;

        try {
            jPlaces = jObject.getJSONArray("predictions");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getPlaces(jPlaces);
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jPlaces){
        int placesCount = jPlaces.length();
        List<HashMap<String, String>> placesList = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> place = null;

        for(int i=0; i< placesCount; i++){
            try {
                place = getPlace((JSONObject)jPlaces.get(i));
                Log.d("PlaceObject", place.toString());
                placesList.add(place);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return placesList;
    }

    private HashMap<String, String> getPlace(JSONObject jPlace){

        HashMap<String, String> place = new HashMap<String, String>();

        String id="";
        String placeid="";
        String description="";

        try {
            description = jPlace.getString("description");
            id = jPlace.getString("id");
            placeid = jPlace.getString("place_id");

            place.put("description", description);
            place.put("_id",id);
            place.put("place_id",placeid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }



    //Parse the Details API
    public String getLocation(String place_id) {
        String jsonData = "";

        String parameters = place_id + "&" + KEY;
        String url = "https://maps.googleapis.com/maps/api/place/details/json?" + parameters;

        try{
            jsonData = PlaceJSONParser.downloadUrl(url);
        }catch(Exception e){
            Log.d("Background Task",e.toString());
        }

        String[] location = new String[2];

        try{
            JSONObject jObject = new JSONObject(jsonData);
            location = parseLocation(jObject);

        }catch(Exception e){
            Log.d("Exception",e.toString());
        }

        return Arrays.toString(location);
    }

    private String[] parseLocation(JSONObject jObject){
        JSONObject jDetails = null;

        try {
            jDetails = jObject.getJSONObject("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getDetails(jDetails);
    }

    private String[] getDetails(JSONObject jDetails){

        String[] location = new String[2];

        try {
            JSONObject jGeometry = jDetails.getJSONObject("geometry");
            JSONObject jLocation = jGeometry.getJSONObject("location");
            location[0] = jLocation.getString("lat");
            location[1] = jLocation.getString("lng");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return location;
    }
}
