package nmct.howest.be.rideshare.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import nmct.howest.be.rideshare.Models.Trip;
import nmct.howest.be.rideshare.R;

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
            card.setTag(position);
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

        holder.txtSearchResultDate.setText(dateString);

        holder.txtSearchResultName.setText(trip.getUserName());

        //Set picture of the person who planned the trip
        String facebookID = trip.getFacebookID();
        holder.imgPicturePlanner.setProfileId(facebookID);

        return card;
    }

    static class ViewHolderItem
    {
        TextView txtSearchResultName;
        TextView txtSearchResultFromTo;
        TextView txtSearchResultDate;
        ProfilePictureView imgPicturePlanner;

        public ViewHolderItem(View row)
        {
            this.txtSearchResultName = (TextView) row.findViewById(R.id.txtSearchResultName);
            this.txtSearchResultFromTo = (TextView) row.findViewById(R.id.txtSearchResultFromTo);
            this.txtSearchResultDate = (TextView) row.findViewById(R.id.txtSearchResultDate);
            this.imgPicturePlanner = (ProfilePictureView) row.findViewById(R.id.imgSearchResult);
        }
    }

}
