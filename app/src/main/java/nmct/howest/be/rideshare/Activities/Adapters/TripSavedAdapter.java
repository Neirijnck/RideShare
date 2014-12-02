package nmct.howest.be.rideshare.Activities.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import nmct.howest.be.rideshare.Activities.Models.Trip;
import nmct.howest.be.rideshare.R;

public class TripSavedAdapter extends ArrayAdapter<Trip>
{

    public TripSavedAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View card = super.getView(position, convertView, parent);

        ViewHolderItem holder = (ViewHolderItem) card.getTag();
        if(holder == null){
            holder = new ViewHolderItem(card);
            card.setTag(holder);
        }

        Trip trip = getItem(position);

        holder.txtTripTitle.setText(trip.getFrom() + " naar " + trip.getTo());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getDefault());
        String dateString="";
        try {
            Date date = sdf.parse(trip.getDatetime());

            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
            dateString = fmtOut.format(date);
        }catch (ParseException ex){
            Log.e("ParseException Date", ex.getMessage());}

        holder.txtTripInfoDate.setText(dateString);


        holder.txtTripInfo.setText("Herhaald: ?");

        if(trip.getPayment()!="") {
            holder.txtTripPrice.setText("Kost: €" + trip.getPayment());
        }
        else
        {
            holder.txtTripPrice.setText("Kost: Gratis");
        }


        return card;
    }

    static class ViewHolderItem
    {
        TextView txtTripTitle;
        TextView txtTripInfoDate;
        TextView txtTripInfo;
        TextView txtTripPrice;

        public ViewHolderItem(View row)
        {
            this.txtTripInfoDate = (TextView) row.findViewById(R.id.txtTripInfoDate);
            this.txtTripInfo = (TextView) row.findViewById(R.id.txtTripInfo);
            this.txtTripPrice = (TextView) row.findViewById(R.id.txtTripPrice);
            this.txtTripTitle = (TextView) row.findViewById(R.id.txtTripTitle);
        }
    }

}
