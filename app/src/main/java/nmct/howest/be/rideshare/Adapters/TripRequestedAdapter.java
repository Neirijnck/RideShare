package nmct.howest.be.rideshare.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

import nmct.howest.be.rideshare.Helpers.Utils;
import nmct.howest.be.rideshare.Models.Match;
import nmct.howest.be.rideshare.Models.Trip;
import nmct.howest.be.rideshare.R;

public class TripRequestedAdapter extends ArrayAdapter<Trip>
{
    private String from="";
    private String to="";
    private String date="";
    private int status=0;
    private String userID="";

    public TripRequestedAdapter(Context context, int resource, int textViewResourceId) {
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

        for(Match match: trip.getMatches())
        {
            from = match.getFrom();
            to = match.getTo();
            date = Utils.parseISOStringToDate(match.getDatetime());
            status = match.getStatus();
            userID = match.getUserID();
        }

        card.setTag(position);

        holder.txtTripInfoDate.setText("Wil meerijden naar " + to);
        holder.txtTripPrice.setText(date);
        holder.txtTripInfo.setText("Status: " + Utils.convertStatus(status));


        //Set picture of the person who planned the trip
        //holder.imgProfilePicture.setProfileId();

        return card;
    }

    static class ViewHolderItem
    {
        TextView txtTripInfoDate;
        TextView txtTripInfo;
        TextView txtTripPrice;
        TextView txtTripTitle;
        ProfilePictureView imgProfilePicture;

        public ViewHolderItem(View row)
        {
            this.txtTripInfoDate = (TextView) row.findViewById(R.id.txtTripInfoDate);
            this.txtTripInfo = (TextView) row.findViewById(R.id.txtTripInfo);
            this.txtTripPrice = (TextView) row.findViewById(R.id.txtTripPrice);
            this.txtTripTitle = (TextView) row.findViewById(R.id.txtTripTitle);
            this.imgProfilePicture = (ProfilePictureView) row.findViewById(R.id.imgTripPicture);
        }
    }
}
