package nmct.howest.be.rideshare.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nmct.howest.be.rideshare.Helpers.Utils;
import nmct.howest.be.rideshare.Models.Review;
import nmct.howest.be.rideshare.R;
import nmct.howest.be.rideshare.RideshareApp;

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.RecyclerViewHolder>
{
    private List<Review> mReviews;

    public ReviewRecyclerAdapter(List<Review> mData) {
        this.mReviews = mData;
    }

    public void updateList(List<Review> reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_review, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position)
    {
        Review review = mReviews.get(position);
        holder.txtReviewName.setText(review.getUserName());
        holder.txtReview.setText(review.getText());

        String dateString= Utils.parseISOStringToDate(review.getDate());
        holder.txtReviewDate.setText(dateString);

        if(review.getScore()!=null) {
            setStars(review.getScore(), holder.star1, holder.star2, holder.star3, holder.star4, holder.star5);
        }else{setStars(0, holder.star1, holder.star2, holder.star3, holder.star4, holder.star5);}
    }

    @Override
    public int getItemCount()
    {
        return mReviews.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtReviewName;
        TextView txtReview;
        TextView txtReviewDate;
        ImageView star1;
        ImageView star2;
        ImageView star3;
        ImageView star4;
        ImageView star5;

        public RecyclerViewHolder(View card)
        {
            super(card);
            this.txtReviewName = (TextView) card.findViewById(R.id.txbBeoordelingNaam);
            this.txtReview = (TextView) card.findViewById(R.id.txbBeoordeling);
            this.txtReviewDate = (TextView) card.findViewById(R.id.txbBeoordelingDate);
            this.star1 = (ImageView) card.findViewById(R.id.imgStar1);
            this.star2 = (ImageView) card.findViewById(R.id.imgStar2);
            this.star3 = (ImageView) card.findViewById(R.id.imgStar3);
            this.star4 = (ImageView) card.findViewById(R.id.imgStar4);
            this.star5 = (ImageView) card.findViewById(R.id.imgStar5);
        }
    }

    private void setStars(int score, ImageView star1, ImageView star2, ImageView star3, ImageView star4, ImageView star5)
    {
        switch (score)
        {
            case 0:
                star1.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star2.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star3.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star4.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star5.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                break;
            case 1:
                star1.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star2.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star3.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star4.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star5.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                break;
            case 2:
                star1.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star2.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star3.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star4.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star5.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                break;
            case 3:
                star1.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star2.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star3.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star4.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star5.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                break;
            case 4:
                star1.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star2.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star3.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star4.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star5.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                break;
            case 5:
                star1.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star2.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star3.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star4.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star5.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                break;
            default:
                star1.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star2.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star3.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star4.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star5.setImageDrawable(RideshareApp.getAppContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                break;

        }
    }


}
