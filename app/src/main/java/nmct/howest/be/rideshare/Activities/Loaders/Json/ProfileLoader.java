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

import nmct.howest.be.rideshare.Activities.Helpers.APIHelper;
import nmct.howest.be.rideshare.Activities.Helpers.Utils;
import nmct.howest.be.rideshare.Activities.Models.Review;
import nmct.howest.be.rideshare.Activities.Models.User;

public class ProfileLoader extends AsyncTaskLoader<User>
{
    private final String mUrl;
    private User user = new User();
    private List<Review> reviews = new ArrayList<Review>();

    public ProfileLoader(Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    @Override
    public User loadInBackground() {

        try {
            loadUser();
        } catch (IOException ex) {
            Log.e("IOException", ex.getMessage());
        }

        return user;
    }

    private void loadUser() throws IOException {
        //Met raw json
        //InputStream in = getContext().getResources().openRawResource(R.raw.preben);

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
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                int id = 0;
                String ID = "";
                String userName = "";
                String facebookLink = "";
                String firstName = "";
                String lastName = "";
                String email = "";
                String gender = "";
                String birthday = "";
                String location = "";
                String carType = "";
                String amountOfSeats="";

                while (reader.hasNext()) {
                    String key = reader.nextName();
                    if (key.equals("_id"))
                    {
                        ID = reader.nextString();
                        user.setID(ID);
                    }
                    else if (key.equals("userName"))
                    {
                        userName = reader.nextString();
                        user.setUserName(userName);
                    }
                    else if (key.equals("facebookLink"))
                    {
                        facebookLink = reader.nextString();
                        user.setFacebookLink(facebookLink);
                    }
                    else if (key.equals("firstName"))
                    {
                        firstName = reader.nextString();
                        user.setFirstName(firstName);
                    }
                    else if (key.equals("lastName"))
                    {
                        lastName = reader.nextString();
                        user.setLastName(lastName);
                    }
                    else if (key.equals("email"))
                    {
                        email = reader.nextString();
                        user.setEmail(email);
                    }
                    else if (key.equals("gender"))
                    {
                        gender = reader.nextString();
                        user.setGender(gender);
                    }
                    else if (key.equals("birthday"))
                    {
                        birthday = reader.nextString();
                        user.setBirthday(birthday);
                    }
                    else if (key.equals("location"))
                    {
                        location = reader.nextString();
                        user.setLocation(location);
                    }
                    else if (key.equals("carType"))
                    {
                        carType = reader.nextString();
                        user.setCarType(carType);
                    }
                    else if (key.equals("amountOfSeats"))
                    {
                        amountOfSeats = reader.nextString();
                        user.setAmountOfSeats(amountOfSeats);
                    }
                    else if(key.equals("reviews"))
                    {
                        reader.beginArray();
                        while(reader.hasNext())
                        {
                            Review review = new Review();
                            reader.beginObject();
                            while(reader.hasNext())
                            {
                                String key_second = reader.nextName();
                                if(key_second.equals("userID")){review.setUserID(reader.nextString());}
                                else if(key_second.equals("userName")){review.setUserName(reader.nextString());}
                                else if(key_second.equals("date")){review.setDate(reader.nextString());}
                                else if(key_second.equals("score")){review.setScore(reader.nextInt());}
                                else if(key_second.equals("text")){review.setText(reader.nextString());}
                                else{reader.skipValue();}
                            }
                            reader.endObject();
                            reviews.add(review);
                        }
                        reader.endArray();
                        user.setReviews(reviews);
                    }
                    else
                    {
                        reader.skipValue();
                    }

                    id++;
                }
            }

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