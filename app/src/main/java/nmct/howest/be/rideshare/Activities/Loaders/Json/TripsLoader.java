package nmct.howest.be.rideshare.Activities.Loaders.Json;

import android.content.Context;
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

import nmct.howest.be.rideshare.Activities.Helpers.Utils;
import nmct.howest.be.rideshare.Activities.Models.Match;
import nmct.howest.be.rideshare.Activities.Models.Message;
import nmct.howest.be.rideshare.Activities.Models.Trip;

public class TripsLoader extends AsyncTaskLoader<List<Trip>>
{
    private final String mUrl;
    private List<Trip> trips = new ArrayList<Trip>();

    public TripsLoader(Context context, String url) {
        super(context);
        mUrl = url;
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
        //Met raw json
        //InputStream in = getContext().getResources().openRawResource(R.raw.trip);

        //Met url json
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(mUrl);
        httpGet.addHeader("Authorization", "000");
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream in = entity.getContent();

        String JSONbody = Utils.convertStreamToString(in);
        Reader stringReader = new StringReader(JSONbody);

        JsonReader reader = new JsonReader(stringReader);
        try
        {
            reader.beginArray();
            while(reader.hasNext()) {
                Trip trip = new Trip();
                reader.beginObject();
                while (reader.hasNext()) {
                    int id = 1;
                    String ID = "";
                    String userID = "";
                    String from = "";
                    String to = "";
                    String dateTime = "";
                    String payment = "";
                    String matchUserID = "";
                    String matchFrom = "";
                    String matchTo = "";
                    String matchDateTime = "";
                    int matchStatus = 0;

                    while (reader.hasNext()) {
                        String key = reader.nextName();
                        if (key.equals("_id")) {
                            ID = reader.nextString();
                            trip.setID(ID);
                        } else if (key.equals("userID")) {
                            userID = reader.nextString();
                            trip.setUserID(userID);
                        } else if (key.equals("from")) {
                            from = reader.nextString();
                            trip.setFrom(from);
                        } else if (key.equals("to")) {
                            to = reader.nextString();
                            trip.setTo(to);
                        } else if (key.equals("datetime")) {
                            dateTime = reader.nextString();
                            trip.setDatetime(dateTime);
                        } else if (key.equals("payment")) {
                            payment = reader.nextString();
                            trip.setPayment(payment);
                        } else if (key.equals("repeat")) {
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
                            trip.setRepeat(repeats);
                        } else if (key.equals("matches")) {
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
                                    } else if (key.equals("status")) {
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
                        id++;
                    }
                }
                reader.endObject();
                trips.add(trip);
            }
            reader.endArray();
        }catch(IOException e)
        {
            Log.e("IOException", e.getMessage());
        }finally
        {
            try{reader.close();}catch(IOException e){}
            try{in.close();}catch(IOException e){}
        }
    }

}
