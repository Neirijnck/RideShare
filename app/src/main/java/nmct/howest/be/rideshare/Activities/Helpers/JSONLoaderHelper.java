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
import java.net.URL;

public abstract class JSONLoaderHelper extends android.support.v4.content.AsyncTaskLoader<Cursor>
{

    private final String[] mColumnNames;
    //Int voor raw json file
    private final int mRawResourceId;
    //String voor url json
    //private final String mRawResourceId;

    private Cursor mCursor;
    private Object lock = new Object();

    public JSONLoaderHelper(Context context, String[] columnNames, int rawResourceId) {
        super(context);

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

            //Met raw json
            InputStream in = getContext().getResources().openRawResource(mRawResourceId);

            //Met url json
            //InputStream in = new URL(mRawResourceId).openStream();

            JsonReader reader = new JsonReader(new InputStreamReader(in));
            try
            {
                reader.beginObject();
                while(reader.hasNext())
                {

                        parse(reader, cursor);

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
