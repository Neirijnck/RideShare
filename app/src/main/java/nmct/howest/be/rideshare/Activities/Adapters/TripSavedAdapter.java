package nmct.howest.be.rideshare.Activities.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

import nmct.howest.be.rideshare.Activities.Helpers.Utils;
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

        card.setTag(position);

        holder.txtTripTitle.setText(trip.getFrom() + " naar " + trip.getTo());

        String dateString= Utils.parseISOStringToDate(trip.getDatetime());
        holder.txtTripInfoDate.setText(dateString);

        boolean[] repeat = trip.getRepeat();

        String repeatString= Utils.parseBoolArrayToDays(repeat);
        holder.txtTripInfo.setText("Herhaald: " + repeatString);


        if(trip.getPayment().equals("") || trip.getPayment().equals("0")) {
            holder.txtTripPrice.setText("Kost: Gratis");
        }
        else
        {
            holder.txtTripPrice.setText("Kost: €" + trip.getPayment());
        }

        //Set picture of the person who planned the trip
        //holder.imgProfilePicture.setProfileId();

        return card;
    }

    static class ViewHolderItem
    {
        TextView txtTripTitle;
        TextView txtTripInfoDate;
        TextView txtTripInfo;
        TextView txtTripPrice;
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
