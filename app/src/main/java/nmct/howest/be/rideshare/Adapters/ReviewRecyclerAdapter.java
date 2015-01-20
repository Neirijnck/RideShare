package nmct.howest.be.rideshare.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

import java.util.List;

import nmct.howest.be.rideshare.Helpers.Utils;
import nmct.howest.be.rideshare.Models.Review;
import nmct.howest.be.rideshare.R;
import nmct.howest.be.rideshare.RideshareApp;

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.RecyclerViewHolder>
{
    //Variables
    private List<Review> mReviews;
    private View mHeader;
    private static final int VIEW_TYPE_HEADER =0;
    private static final int VIEW_TYPE_ITEM=1;

    public ReviewRecyclerAdapter(View header, List<Review> mData) {
        this.mReviews = mData;
        this.mHeader = header;
    }

    public void updateList(View header, List<Review> reviews) {
        mReviews = reviews;
        mHeader = header;
        notifyDataSetChanged();
    }

    public boolean isHeader(int position)
    {
        return position ==0;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == VIEW_TYPE_HEADER) {
            return new RecyclerViewHolder(mHeader);
        }
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_review, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ?
                VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position)
    {
        if (isHeader(position)) {
            return;
        }

        Review review = mReviews.get(position-1);

        holder.txtReviewName.setText(review.getUserName());
        holder.imgReviewPic.setProfileId(review.getFacebookID());
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
        return mReviews.size()+1;
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
        ProfilePictureView imgReviewPic;

        public RecyclerViewHolder(View card)
        {
            super(card);
            this.txtReviewName = (TextView) card.findViewById(R.id.txbReviewName);
            this.txtReview = (TextView) card.findViewById(R.id.txbReview);
            this.txtReviewDate = (TextView) card.findViewById(R.id.txbReviewDate);
            this.star1 = (ImageView) card.findViewById(R.id.imgStar1);
            this.star2 = (ImageView) card.findViewById(R.id.imgStar2);
            this.star3 = (ImageView) card.findViewById(R.id.imgStar3);
            this.star4 = (ImageView) card.findViewById(R.id.imgStar4);
            this.star5 = (ImageView) card.findViewById(R.id.imgStar5);
            this.imgReviewPic = (ProfilePictureView) card.findViewById(R.id.imgReviewPic);
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
