package nmct.howest.be.rideshare.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import nmct.howest.be.rideshare.Helpers.Utils;
import nmct.howest.be.rideshare.Models.Message;
import nmct.howest.be.rideshare.R;

public class MessagesAdapter extends ArrayAdapter<Message>
{
    //Variables
    private String userID = "";
    private String datetime = "";
    private String text = "";

    public MessagesAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);

        ViewHolderItem holder = (ViewHolderItem) v.getTag();
        if(holder == null){
            holder = new ViewHolderItem(v);
            v.setTag(holder);
        }

        Message m = getItem(position);
        holder.txbMessageText.setText(m.getText());
        holder.txbMessageDate.setText(Utils.parseISOStringToDate(m.getDatetime()));

        return v;
    }

    static class ViewHolderItem
    {
        TextView txbMessageText;
        TextView txbMessageDate;

        public ViewHolderItem(View row)
        {
            this.txbMessageText = (TextView) row.findViewById(R.id.txbMessageText);
            this.txbMessageDate = (TextView) row.findViewById(R.id.txbMessageDate);
        }
    }

}
