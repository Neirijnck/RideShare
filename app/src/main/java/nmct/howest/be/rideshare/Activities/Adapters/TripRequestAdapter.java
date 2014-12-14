package nmct.howest.be.rideshare.Activities.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import nmct.howest.be.rideshare.Activities.Models.Trip;
import nmct.howest.be.rideshare.R;

public class TripRequestAdapter extends ArrayAdapter<Trip>
{
    public TripRequestAdapter(Context context, int resource, int textViewResourceId) {
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

        holder.txtTripInfoDate.setText("Request to");
        holder.txtTripInfo.setText(trip.getTo());

        return card;
    }

    static class ViewHolderItem
    {
        TextView txtTripInfoDate;
        TextView txtTripInfo;

        public ViewHolderItem(View row)
        {
            this.txtTripInfoDate = (TextView) row.findViewById(R.id.txtTripInfoDate);
            this.txtTripInfo = (TextView) row.findViewById(R.id.txtTripInfo);
        }
    }

}
