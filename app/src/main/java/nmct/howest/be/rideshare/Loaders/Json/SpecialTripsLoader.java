package nmct.howest.be.rideshare.Loaders.Json;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;
import android.util.JsonReader;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import nmct.howest.be.rideshare.Helpers.Utils;
import nmct.howest.be.rideshare.Models.Match;
import nmct.howest.be.rideshare.Models.Message;
import nmct.howest.be.rideshare.Models.Trip;

public class SpecialTripsLoader extends AsyncTaskLoader<List<Trip>>
{
    private final String mUrl;
    private List<Trip> trips = new ArrayList<Trip>();
    private Context context;

    public SpecialTripsLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
        this.context = context;
    }

    @Override
    public List<Trip> loadInBackground()
    {
        try {
            loadTrips();
        }catch (IOException ex)
        {
            Log.e("IOException", ex.getMessage());
        }

        return trips;
    }

    private void loadTrips() throws IOException
    {
        //Url json
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(mUrl);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String token = pref.getString("accessToken", "");
        httpGet.addHeader("Authorization", token);
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream in = entity.getContent();

        String JSONbody = Utils.convertStreamToString(in);
        Reader stringReader = new StringReader(JSONbody);

        JsonReader reader = new JsonReader(stringReader);
        try
        {
            reader.beginArray();
            while(reader.hasNext())
            {
                Trip trip = new Trip();
                reader.beginObject();
                while (reader.hasNext()) {

                    String ID = "";
                    String matchID="";
                    String matchUserID = "";
                    String matchFrom = "";
                    String matchTo = "";
                    String matchDateTime = "";
                    int matchStatus=0;

                    while (reader.hasNext()) {
                        String key = reader.nextName();
                        if (key.equals("_id")) {
                            ID = reader.nextString();
                            trip.setID(ID);
                        }else if (key.equals("matches")) {
                            List<Match> matches = new ArrayList<Match>();
                            reader.beginArray();
                            while (reader.hasNext()) {
                                Match match = new Match();
                                reader.beginObject();
                                while (reader.hasNext()) {
                                    String key_third = reader.nextName();
                                    if (key_third.equals("userID")) {
                                        matchUserID = reader.nextString();
                                        match.setUserID(matchUserID);
                                    }else if (key_third.equals("_id")) {
                                        matchID = reader.nextString();
                                        match.setId(matchID);
                                    } else if (key_third.equals("from")) {
                                        matchFrom = reader.nextString();
                                        match.setFrom(matchFrom);
                                    } else if (key_third.equals("to")) {
                                        matchTo = reader.nextString();
                                        match.setTo(matchTo);
                                    } else if (key_third.equals("datetime")) {
                                        matchDateTime = reader.nextString();
                                        match.setDatetime(matchDateTime);
                                    } else if (key_third.equals("messages")) {
                                        List<Message> messages = new ArrayList<Message>();
                                        reader.beginArray();
                                        while (reader.hasNext()) {
                                            Message message = new Message();
                                            reader.beginObject();
                                            while (reader.hasNext()) {
                                                String key_four = reader.nextName();
                                                if (key_four.equals("userID")) {
                                                    message.setUserID(reader.nextString());
                                                } else if (key_four.equals("datetime")) {
                                                    message.setDatetime(reader.nextString());
                                                } else if (key_four.equals("text")) {
                                                    message.setText(reader.nextString());
                                                } else {
                                                    reader.skipValue();
                                                }
                                            }
                                            reader.endObject();
                                            messages.add(message);
                                        }
                                        reader.endArray();
                                        match.setMessages(messages);
                                    } else if (key_third.equals("status")) {
                                        matchStatus = reader.nextInt();
                                        match.setStatus(matchStatus);
                                    } else {
                                        reader.skipValue();
                                    }
                                }
                                reader.endObject();
                                matches.add(match);
                            }
                            reader.endArray();
                            trip.setMatches(matches);
                        } else {
                            reader.skipValue();
                        }
                    }
                }
                reader.endObject();
                trips.add(trip);
            }
            reader.endArray();
        }
        catch(IOException e)
        {
            Log.e("IOException", e.getMessage());
        }finally
        {
            try{reader.close();}catch(IOException e){ Log.e("IOException", e.getMessage()); }
            try{in.close();}catch(IOException e){ Log.e("IOException", e.getMessage()); }
        }
    }

}
