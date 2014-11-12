package nmct.howest.be.rideshare.Activities.Loaders;

import android.content.Context;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.util.JsonReader;
import java.io.IOException;
import nmct.howest.be.rideshare.Activities.Helpers.JSONLoaderHelper;
import nmct.howest.be.rideshare.R;

/**
 * Created by Preben on 12/11/2014.
 */
public class TripLoader extends JSONLoaderHelper
{

    public TripLoader(Context context) {
        super(context, "trip", new String[]{BaseColumns._ID, "userID","from", "to", "datetime", "payment","repeat","matches"}, R.raw.trip);
    }

    @Override
    protected void parse(JsonReader reader, MatrixCursor cursor) throws IOException {
        int id=1;
        reader.beginArray();
        while(reader.hasNext())
        {
            reader.beginObject();
            reader.nextName();
            String userID = reader.nextString();
            reader.nextName();
            String from = reader.nextString();
            reader.nextName();
            String to = reader.nextString();
            reader.nextName();
            String dateTime = reader.nextString();
            reader.nextName();
            String payment = reader.nextString();

            MatrixCursor.RowBuilder builder = cursor.newRow();
            builder.add(userID);
            builder.add(from);
            builder.add(to);
            builder.add(dateTime);
            builder.add(payment);

            id++;

        }
        reader.endArray();
    }
}
