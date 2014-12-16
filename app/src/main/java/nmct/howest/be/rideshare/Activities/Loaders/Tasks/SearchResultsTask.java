package nmct.howest.be.rideshare.Activities.Loaders.Tasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import nmct.howest.be.rideshare.Activities.Adapters.SearchResultAdapter;
import nmct.howest.be.rideshare.Activities.DetailsActivity;
import nmct.howest.be.rideshare.Activities.Helpers.Utils;
import nmct.howest.be.rideshare.Activities.Models.Match;
import nmct.howest.be.rideshare.Activities.Models.Message;
import nmct.howest.be.rideshare.Activities.Models.Trip;
import nmct.howest.be.rideshare.R;
import nmct.howest.be.rideshare.RideshareApp;

/**
 * Created by Preben on 4/12/2014.
 */
public class SearchResultsTask extends AsyncTask<Bundle, Void, List<Trip>>
{
    private final ProgressBar mProgress;
    private LinearLayout lstSearchResults;
    private ArrayAdapter<Trip> mAdapterSearchResults;
    private TextView mTxtNoResults;
    private ScrollView mLayoutSearchResults;
    private String token;

    public SearchResultsTask(String fbToken, final ProgressBar progress, LinearLayout listSearchResults, TextView txtNoResults, ScrollView layout_search_results)
    {
        this.mProgress = progress;
        this.lstSearchResults = listSearchResults;
        this.mTxtNoResults = txtNoResults;
        this.mLayoutSearchResults = layout_search_results;
        this.token = fbToken;
    }

    @Override
    protected List<Trip> doInBackground(Bundle... params)
    {
        List<Trip> trips = new ArrayList<>();
        Trip trip = new Trip();

        Bundle b = params[0];
        String fromCity = b.getString("from");
        String toCity = b.getString("to");
        String date = b.getString("date");
        String time = b.getString("time");
        boolean share = b.getBoolean("share");

        String datetime= Utils.parseDateToISOString(date, time);

        //Search trips from api with these parameters
        try {
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost("http://188.226.154.228:8080/api/v1/trips/search");
            httppost.addHeader("Authorization", token);

            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("from", fromCity.trim()));
            parameters.add(new BasicNameValuePair("to", toCity.trim()));

            if(!datetime.isEmpty())
                parameters.add(new BasicNameValuePair("datetime", datetime));

            httppost.setEntity(new UrlEncodedFormEntity(parameters));

            HttpResponse response = httpclient.execute(httppost);

            HttpEntity entity = response.getEntity();
            String result = Utils.convertStreamToString(entity.getContent());
            Reader stringReader = new StringReader(result);
            JsonReader reader = new JsonReader(stringReader);

            //Parse json to trips
            try
            {
                reader.beginArray();
                while(reader.hasNext()) {
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

                    //Get facebookID and name for picture
                    String facebookID = Utils.getUserFacebookIDFromUserID(token, trip.getUserID());
                    trip.setFacebookID(facebookID);
                    trip.setUserName(Utils.getUserNameFromUserID(token, trip.getUserID()));

                    trips.add(trip);
                }
                reader.endArray();
            }catch(IOException e)
            {
                Log.e("IOException", e.getMessage());
            }finally
            {
                try{reader.close();}catch(IOException e){}
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }


        return trips;
    }


    @Override
    protected void onPostExecute(List<Trip> result)
    {
        //Hide the progressbar after loading
        mProgress.setVisibility(View.INVISIBLE);

        mAdapterSearchResults = new SearchResultAdapter(RideshareApp.getAppContext(), R.layout.card_search_result, R.id.txtSearchResultName);
        mAdapterSearchResults.addAll(result);
        final List<Trip> mTrips = result;
        //Adding items to linear layouts
        int adaptercountSearchResults = mAdapterSearchResults.getCount();
        for(int i =0; i < adaptercountSearchResults; i++)
        {
            View item = mAdapterSearchResults.getView(i, null, null);

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RideshareApp.getAppContext(), DetailsActivity.class);
                    Bundle b = new Bundle();
                    int pos = (int) v.getTag();
                    String id = mTrips.get(pos).getID();
                    Log.d("id",""+ id);
                    b.putString("id", id);
                    b.putInt("type", 3);
                    intent.putExtras(b);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    RideshareApp.getAppContext().startActivity(intent);
                }
            });

            lstSearchResults.addView(item);
        }

        //Empty, no results
        if(adaptercountSearchResults==0)
        {
                mTxtNoResults.setVisibility(View.VISIBLE);
        }
        else
        {
            mLayoutSearchResults.setVisibility(View.VISIBLE);
        }

    }
}
