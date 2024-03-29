package nmct.howest.be.rideshare.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.tonicartos.superslim.LayoutManager;

import java.util.ArrayList;
import java.util.List;

import nmct.howest.be.rideshare.Activities.DetailsActivity;
import nmct.howest.be.rideshare.Helpers.Utils;
import nmct.howest.be.rideshare.Models.Match;
import nmct.howest.be.rideshare.Models.Trip;
import nmct.howest.be.rideshare.R;
import nmct.howest.be.rideshare.RideshareApp;


/**
 * Created by Preben on 15/01/2015.
 */
public class TripRecyclerAdapter extends RecyclerView.Adapter<TripRecyclerAdapter.TripRecyclerViewHolder>
{
    //Variables
    private static final int VIEW_TYPE_HEADER = 0x01;
    private static final int VIEW_TYPE_CONTENT = 0x00;

    private final ArrayList<LineItem> mItems;
    private List<Trip> mTrips;
    private Context mContext;
    private SharedPreferences pref;
    private String myUserID;

    public TripRecyclerAdapter(Context context, List<Trip> mData)
    {
        this.mTrips = mData;
        this.mItems = new ArrayList<LineItem>();
        this.mContext = context;

        pref = PreferenceManager.getDefaultSharedPreferences(context);
        myUserID = pref.getString("myUserID", "");

        //Insert headers into list of items
        String lastHeader = "";
        int sectionCount = 0;
        int headerCount = 0;
        int sectionFirstPosition = 0;
        for(int i = 0; i<mTrips.size();i++)
        {
            Trip trip = mTrips.get(i);

            String header = trip.getType();
            //Make sections for requests, requested and saved trips
            //Make difference between type of trip
            if(!TextUtils.equals(lastHeader, header))
            {
                sectionCount +=1;
                sectionFirstPosition = i + headerCount;
                lastHeader = header;
                headerCount +=1;
                //Add new header
                mItems.add(new LineItemHeader(lastHeader, sectionCount, sectionFirstPosition, true));
            }
            //Add the card
            mItems.add(new LineItemTrip(trip, sectionCount, sectionFirstPosition, false));
        }
        mContext = context;
    }

    public void updateList(List<Trip> trips) {
        mTrips = trips;
        notifyDataSetChanged();
    }

    @Override
    public TripRecyclerAdapter.TripRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView;
        //Check if the view is a header or content
        if(viewType == VIEW_TYPE_HEADER)
        {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.header, parent, false);
        }
        else
        {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.card_trip, parent, false);
        }
        return new TripRecyclerViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).isHeader ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onBindViewHolder(TripRecyclerAdapter.TripRecyclerViewHolder holder, int position)
    {
        //Get the lineitem
        LineItem item = mItems.get(position);
        View itemView = holder.itemView;

        //Header or content?
        if(item.isHeader)
        {
            //Set header
            TextView header = (TextView) itemView.findViewById(R.id.header);
            header.setText(((LineItemHeader)item).text);
        }
        else
        {
            //3 types of layouts, for each type of trip
            //Set card
            final Trip trip = ((LineItemTrip) item).trip;

            if(trip.getType().equals(mContext.getResources().getString(R.string.Trip_SavedTrips)))
            {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, DetailsActivity.class);
                        Bundle b = new Bundle();
                        b.putString("id", trip.getID());
                        b.putInt("type", 2);
                        intent.putExtras(b);
                        mContext.startActivity(intent);
                    }
                });

                holder.txtTripTitle.setText(trip.getFrom() + RideshareApp.getAppContext().getResources().getString(R.string.adapterTo) + trip.getTo());

                String dateString= Utils.parseISOStringToDate(trip.getDatetime());
                holder.txtTripInfoDate.setText(dateString);

                boolean[] repeat = trip.getRepeat();

                String repeatString= Utils.parseBoolArrayToDays(repeat);
                holder.txtTripInfo.setText(RideshareApp.getAppContext().getResources().getString(R.string.adapterRepeat) + repeatString);


                if(trip.getPayment().equals("") || trip.getPayment().equals("0")) {
                    holder.txtTripPrice.setText(RideshareApp.getAppContext().getResources().getString(R.string.adapterCostFree));
                }
                else
                {
                    holder.txtTripPrice.setText(RideshareApp.getAppContext().getResources().getString(R.string.adapterCostPay) + trip.getPayment());
                }

                //Set picture of the person who planned the trip
                holder.imgProfilePicture.setProfileId(trip.getFacebookID());
            }
            else if(trip.getType().equals(mContext.getResources().getString(R.string.Trip_MyRequests)))
            {
                final Match m = new Match();
                for(Match match: trip.getMatches())
                {
                    m.setId(match.getId());
                    m.setFrom(match.getFrom());
                    m.setTo(match.getTo());
                    m.setDatetime(Utils.parseISOStringToDate(match.getDatetime()));
                    m.setStatus(match.getStatus());
                    m.setUserID(match.getUserID());
                    m.setFacebookID(match.getFacebookID());
                    m.setUserName(match.getUserName());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, DetailsActivity.class);
                        Bundle b = new Bundle();
                        b.putString("id", trip.getID());
                        b.putString("matchID", m.getId());
                        b.putInt("type", 1);
                        intent.putExtras(b);
                        mContext.startActivity(intent);
                    }
                });

                holder.txtTripTitle.setText(m.getUserName());
                holder.txtTripInfoDate.setText(RideshareApp.getAppContext().getResources().getString(R.string.adapterRequestTo) + m.getTo());
                holder.txtTripInfo.setText(RideshareApp.getAppContext().getResources().getString(R.string.adapterStatus) + Utils.convertStatus(m.getStatus()));
                if (m.getStatus() == 1) {
                    holder.txtTripInfo.setTextColor(RideshareApp.getAppContext().getResources().getColor(R.color.rideshare_text_green));
                } else if (m.getStatus() == 2)
                    holder.txtTripInfo.setTextColor(RideshareApp.getAppContext().getResources().getColor(R.color.rideshare_text_red));
                {
                }
                holder.txtTripPrice.setText(m.getDatetime());

                //Set picture of the person who planned the trip
                holder.imgProfilePicture.setProfileId(m.getFacebookID());
                }
            }
            else if(trip.getType().equals(mContext.getResources().getString(R.string.Trip_Requests)))
            {
                final Match m = new Match();
                for(Match match: trip.getMatches())
                {
                    m.setId(match.getId());
                    m.setFrom(match.getFrom());
                    m.setTo(match.getTo());
                    m.setDatetime(Utils.parseISOStringToDate(match.getDatetime()));
                    m.setStatus(match.getStatus());
                    m.setUserID(match.getUserID());
                    m.setFacebookID(match.getFacebookID());
                    m.setUserName(match.getUserName());
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, DetailsActivity.class);
                        Bundle b = new Bundle();
                        b.putString("id", trip.getID());
                        b.putString("matchID", m.getId());
                        b.putInt("type", 0);
                        intent.putExtras(b);
                        mContext.startActivity(intent);
                    }
                });

                holder.txtTripTitle.setText(m.getUserName());
                holder.txtTripInfoDate.setText(RideshareApp.getAppContext().getResources().getString(R.string.adapterRequestedTo) + m.getTo());
                holder.txtTripPrice.setText(m.getDatetime());
                holder.txtTripInfo.setText(RideshareApp.getAppContext().getResources().getString(R.string.adapterStatus) + Utils.convertStatus(m.getStatus()));
                if(m.getStatus()==1){holder.txtTripInfo.setTextColor(RideshareApp.getAppContext().getResources().getColor(R.color.rideshare_text_green));}
                else if(m.getStatus()==2)holder.txtTripInfo.setTextColor(RideshareApp.getAppContext().getResources().getColor(R.color.rideshare_text_red));{}

                //Set picture of the person who planned the trip
                holder.imgProfilePicture.setProfileId(m.getFacebookID());
            }
        }

        final LayoutManager.LayoutParams lp = (LayoutManager.LayoutParams) itemView
                .getLayoutParams();

        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.section = item.section;
        lp.sectionFirstPosition = item.sectionFirstPosition;
        itemView.setLayoutParams(lp);
    }

    public static class TripRecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtTripTitle;
        TextView txtTripInfoDate;
        TextView txtTripInfo;
        TextView txtTripPrice;
        ProfilePictureView imgProfilePicture;

        public TripRecyclerViewHolder(View card)
        {
            super(card);
            this.txtTripInfoDate = (TextView) card.findViewById(R.id.txtTripInfoDate);
            this.txtTripInfo = (TextView) card.findViewById(R.id.txtTripInfo);
            this.txtTripPrice = (TextView) card.findViewById(R.id.txtTripPrice);
            this.txtTripTitle = (TextView) card.findViewById(R.id.txtTripTitle);
            this.imgProfilePicture = (ProfilePictureView) card.findViewById(R.id.imgTripPicture);
        }
    }

    private static class LineItem
    {
        public int section;

        public int sectionFirstPosition;

        public boolean isHeader;

        public LineItem(int section, int sectionFirstPosition, boolean isHeader)
        {
            this.section = section;
            this.sectionFirstPosition = sectionFirstPosition;
            this.isHeader = isHeader;
        }

        private LineItem() {
        }
    }

    //Line item header
    private static class LineItemHeader extends LineItem
    {
        public String text;

        public LineItemHeader(String text, int section, int sectionFirstPosition, boolean isHeader) {
            this.text = text;
            this.section = section;
            this.sectionFirstPosition = sectionFirstPosition;
            this.isHeader = isHeader;
        }
    }

    //Line item content
    private static class LineItemTrip extends LineItem
    {
        public Trip trip;

        public LineItemTrip(Trip trip, int section, int sectionFirstPosition, boolean isHeader)
        {
            this.trip = trip;
            this.section = section;
            this.sectionFirstPosition = sectionFirstPosition;
            this.isHeader = isHeader;
        }
    }

}
