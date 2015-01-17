package nmct.howest.be.rideshare.Adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import nmct.howest.be.rideshare.Activities.DetailsActivity;
import nmct.howest.be.rideshare.Models.Trip;
import nmct.howest.be.rideshare.R;
import nmct.howest.be.rideshare.RideshareApp;

/**
 * Created by Preben on 17/01/2015.
 */
public class SearchResultRecyclerAdapter extends RecyclerView.Adapter<SearchResultRecyclerAdapter.SearchResultRecyclerViewHolder>
{
    //Variables
    private List<Trip> mSearchResults;
    private int lastPosition = -1;

    public SearchResultRecyclerAdapter(List<Trip> mData) {
        this.mSearchResults = mData;
    }

    public void updateList(List<Trip> results) {
        mSearchResults = results;
        notifyDataSetChanged();
    }

    @Override
    public SearchResultRecyclerAdapter.SearchResultRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_search_result, parent, false);
        return new SearchResultRecyclerAdapter.SearchResultRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchResultRecyclerAdapter.SearchResultRecyclerViewHolder holder, int position)
    {
        final Trip tripResult = mSearchResults.get(position);

        //Animation
        setAnimation(holder.itemView, position);

        holder.txtSearchResultFromTo.setText(tripResult.getFrom() + " naar " + tripResult.getTo());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getDefault());
        String dateString="";
        try {
            Date date = sdf.parse(tripResult.getDatetime());

            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
            dateString = fmtOut.format(date);
        }catch (ParseException ex){
            Log.e("ParseException Date", ex.getMessage());}

        holder.txtSearchResultDate.setText(dateString);

        holder.txtSearchResultName.setText(tripResult.getUserName());

        //Set picture of the person who planned the trip
        String facebookID = tripResult.getFacebookID();
        holder.imgPicturePlanner.setProfileId(facebookID);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RideshareApp.getAppContext(), DetailsActivity.class);
                Bundle b = new Bundle();
                String id = tripResult.getID();
                b.putString("id", id);
                b.putInt("type", 3);
                intent.putExtras(b);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                RideshareApp.getAppContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSearchResults.size();
    }

    public static class SearchResultRecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtSearchResultName;
        TextView txtSearchResultFromTo;
        TextView txtSearchResultDate;
        ProfilePictureView imgPicturePlanner;

        public SearchResultRecyclerViewHolder(View card)
        {
            super(card);
            this.txtSearchResultName = (TextView) card.findViewById(R.id.txtSearchResultName);
            this.txtSearchResultFromTo = (TextView) card.findViewById(R.id.txtSearchResultFromTo);
            this.txtSearchResultDate = (TextView) card.findViewById(R.id.txtSearchResultDate);
            this.imgPicturePlanner = (ProfilePictureView) card.findViewById(R.id.imgSearchResult);
        }
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(RideshareApp.getAppContext(), android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
