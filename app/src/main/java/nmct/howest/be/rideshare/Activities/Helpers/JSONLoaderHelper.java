package nmct.howest.be.rideshare.Activities.Helpers;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.util.JsonReader;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Preben on 12/11/2014.
 */
public abstract class JSONLoaderHelper extends android.support.v4.content.AsyncTaskLoader<Cursor>
{

    private final String[] mColumnNames;
    private final String mPropertyName;
    private final int mRawResourceId;

    private Cursor mCursor;
    private Object lock = new Object();

    public JSONLoaderHelper(Context context, String propertyName, String[] columnNames, int rawResourceId) {
        super(context);
        mPropertyName = propertyName;
        mColumnNames = columnNames;
        mRawResourceId = rawResourceId;
    }

    @Override
    protected void onStartLoading() {
        if(mCursor != null)
        {
            deliverResult(mCursor);
        }
        if(takeContentChanged() || mCursor ==null)
        {
            forceLoad();
        }
    }

    @Override
    public Cursor loadInBackground()
    {
        if(mCursor==null)
        {
            loadCursor();
        }
        return mCursor;
    }

    private void loadCursor()
    {
        synchronized (lock)
        {
            if(mCursor != null) return;

            MatrixCursor cursor = new MatrixCursor(mColumnNames);
            InputStream in = getContext().getResources().openRawResource(mRawResourceId);

            JsonReader reader = new JsonReader(new InputStreamReader(in));
            try
            {
                reader.beginObject();
                while(reader.hasNext())
                {
                    String propName = reader.nextName();
                    if(propName.equals(mPropertyName))
                    {
                        parse(reader, cursor);
                    }
                    else
                    {
                        reader.skipValue();
                    }
                }
                reader.endObject();
            }catch(IOException e)
            {
                Log.e("IOException", e.getMessage());
            }finally
            {
                try{reader.close();}catch(IOException e){}
                try{in.close();}catch(IOException e){}
            }
            mCursor = cursor;
        }
    }
    protected abstract void parse(JsonReader reader, MatrixCursor cursor)throws IOException;
}
