package nmct.howest.be.rideshare.Loaders.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.SimpleAdapter;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import nmct.howest.be.rideshare.Loaders.Json.PlaceJSONParser;

// Get all places from Google Places AutoComplete API
public class PlacesTask extends AsyncTask<String, Void, String> {

    public AutoCompleteTextView view;
    public Context context;

    public PlacesTask(AutoCompleteTextView view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... place) {
        String data = "";
        String input = "";

        try {
            input = "input=" + URLEncoder.encode(place[0], "utf-8");
            Log.d("Input", input);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        String parameters = input + "&" + "types=address&sensor=false&" + PlaceJSONParser.KEY;
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?" + parameters;

        try{
            data = PlaceJSONParser.downloadUrl(url);
        }catch(Exception e){
            Log.d("Background Task",e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        ParserTask parserTask = new ParserTask();
        parserTask.execute(result);
    }

    /* Parse Google Places in JSON format */
    class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                JSONObject jObject = new JSONObject(jsonData[0]);
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[] { "description"};
            int[] to = new int[] { android.R.id.text1 };

            PlaceJSONParser.PLACES = result;

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(context, result, android.R.layout.simple_list_item_1, from, to);
            view.setAdapter(adapter);
        }
    }
}



