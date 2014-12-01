package nmct.howest.be.rideshare.Activities.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
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

        holder.txtTripInfo1.setText("Request to");
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
