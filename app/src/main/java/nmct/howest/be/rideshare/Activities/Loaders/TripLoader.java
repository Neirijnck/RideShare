package nmct.howest.be.rideshare.Activities.Loaders;

import android.content.Context;
import android.database.MatrixCursor;
import android.util.JsonReader;

import java.io.IOException;

import nmct.howest.be.rideshare.Activities.Helpers.JsonHelperTest;

/**
 * Created by Preben on 12/11/2014.
 */
public class TripLoader extends JsonHelperTest
{

    public TripLoader(Context context, String propertyName, String[] columnNames, int rawResourceId) {
        super(context, propertyName, columnNames, rawResourceId);
    }

    @Override
    protected void parse(JsonReader reader, MatrixCursor cursor) throws IOException {

    }
}
