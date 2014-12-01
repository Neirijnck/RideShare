package nmct.howest.be.rideshare.Activities.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import nmct.howest.be.rideshare.Activities.Models.Trip;
import nmct.howest.be.rideshare.R;

/**
 * Created by Preben on 28/11/2014.
 */
public class TripRequestedAdapter extends ArrayAdapter<Trip>
{
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

        holder.txtTripInfo1.setText("Requested to");
        holder.txtTripInfo2.setText(trip.getTo());

        return card;
    }

    static class ViewHolderItem
    {
        TextView txtTripInfo1;
        TextView txtTripInfo2;

        public ViewHolderItem(View row)
        {
            this.txtTripInfo1 = (TextView) row.findViewById(R.id.txtTripInfo1);
            this.txtTripInfo2 = (TextView) row.findViewById(R.id.txtTripInfo2);
        }
    }
}
