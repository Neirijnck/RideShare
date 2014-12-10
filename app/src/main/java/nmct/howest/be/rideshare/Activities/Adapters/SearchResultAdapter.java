package nmct.howest.be.rideshare.Activities.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import nmct.howest.be.rideshare.Activities.Models.Trip;
import nmct.howest.be.rideshare.R;

/**
 * Created by Preben on 9/12/2014.
 */
public class SearchResultAdapter extends ArrayAdapter<Trip>
{

    public SearchResultAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View card =  super.getView(position, convertView, parent);


        ViewHolderItem holder = (ViewHolderItem) card.getTag();
        if(holder == null){
            holder = new ViewHolderItem(card);
            card.setTag(holder);
        }

        Trip trip = getItem(position);

        holder.txtSearchResultFromTo.setText(trip.getFrom() + " naar " + trip.getTo());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getDefault());
        String dateString="";
        try {
            Date date = sdf.parse(trip.getDatetime());

            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
            dateString = fmtOut.format(date);
        }catch (ParseException ex){
            Log.e("ParseException Date", ex.getMessage());}

        holder.txtSearchResultName.setText(dateString);

        return card;
    }

    static class ViewHolderItem
    {
        TextView txtSearchResultName;
        TextView txtSearchResultFromTo;

        public ViewHolderItem(View row)
        {
            this.txtSearchResultName = (TextView) row.findViewById(R.id.txtSearchResultName);
            this.txtSearchResultFromTo = (TextView) row.findViewById(R.id.txtSearchResultFromTo);
        }
    }

}
