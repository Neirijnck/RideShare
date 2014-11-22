package nmct.howest.be.rideshare.Activities.Loaders;

import android.content.Context;
import android.database.MatrixCursor;
import android.util.JsonReader;

import java.io.IOException;

import nmct.howest.be.rideshare.Activities.Helpers.JSONLoaderHelper;
import nmct.howest.be.rideshare.R;

/**
 * Created by Preben on 15/11/2014.
 */
public class ProfileLoader extends JSONLoaderHelper
{
    public ProfileLoader(Context context)
    {
        super(context, new String[]{"_id","userName", "facebookLink", "firstName", "lastName","email","gender", "birthday", "location","carType","amountOfSeats","reviews"}, R.raw.preben);
    }

    @Override
    protected void parse(JsonReader reader, MatrixCursor cursor) throws IOException
    {
        int id=0;
        String ID="";
        String userName="";
        String facebookLink="";
        String firstName="";
        String lastName="";
        String email="";
        String gender="";
        String birthday="";
        String location="";
        String carType="";
        String amountOfSeats="";

        while(reader.hasNext())
        {
            String key = reader.nextName();
            if(key.equals("_id")){ID = reader.nextString();}
            else if(key.equals("userName")){userName = reader.nextString();}
            else if(key.equals("facebookLink")){facebookLink= reader.nextString();}
            else if(key.equals("firstName")){firstName= reader.nextString();}
            else if(key.equals("lastName")){lastName= reader.nextString();}
            else if(key.equals("email")){email= reader.nextString();}
            else if(key.equals("gender")){gender= reader.nextString();}
            else if(key.equals("birthday")){birthday= reader.nextString();}
            else if(key.equals("location")){location= reader.nextString();}
            else if(key.equals("carType")){carType= reader.nextString();}
            else if(key.equals("amountOfSeats")){amountOfSeats= reader.nextString();}
            else{reader.skipValue();}

            MatrixCursor.RowBuilder builder = cursor.newRow();
            builder.add(ID);
            builder.add(userName);
            builder.add(facebookLink);
            builder.add(firstName);
            builder.add(lastName);

            id++;
        }

/*        builder.add(email);
        builder.add(gender);
        builder.add(birthday);
        builder.add(location);
        builder.add(carType);
        builder.add(amountOfSeats);*/
    }
}
