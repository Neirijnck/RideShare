package nmct.howest.be.rideshare.Activities.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nmct.howest.be.rideshare.R;

public class TripRequestCursorAdapter extends CursorAdapter
{
    private Cursor mCursor;
    private Context mContext;
    private final LayoutInflater mInflater;


    public TripRequestCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        final View view=mInflater.inflate(R.layout.card_trip,parent,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        TextView tripInfo1 =(TextView)view.findViewById(R.id.txtTripInfo1);
        tripInfo1.setText(cursor.getString(cursor.getColumnIndex("from")));

    }

}
