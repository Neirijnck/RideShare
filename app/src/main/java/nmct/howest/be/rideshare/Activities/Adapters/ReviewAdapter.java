package nmct.howest.be.rideshare.Activities.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import nmct.howest.be.rideshare.Activities.Models.Review;
import nmct.howest.be.rideshare.R;

/**
 * Created by Preben on 29/11/2014.
 */
public class ReviewAdapter extends ArrayAdapter<Review>
{

    public ReviewAdapter(Context context, int resource, int textViewResourceId) {
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

        Review review = getItem(position);
        holder.txtReviewName.setText(review.getUserName());
        holder.txtReview.setText(review.getText());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getDefault());
        String reviewDate="";
        try {
            Date date = sdf.parse(review.getDate());

            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
            reviewDate = fmtOut.format(date);
        }catch (ParseException ex){
            Log.e("ParseException Date", ex.getMessage());}

        holder.txtReviewDate.setText(reviewDate);


        ImageView star1 = (ImageView) card.findViewById(R.id.imgStar1);
        ImageView star2 = (ImageView) card.findViewById(R.id.imgStar2);
        ImageView star3 = (ImageView) card.findViewById(R.id.imgStar3);
        ImageView star4 = (ImageView) card.findViewById(R.id.imgStar4);
        ImageView star5 = (ImageView) card.findViewById(R.id.imgStar5);

        setStars(review.getScore(), star1, star2, star3, star4, star5);

        return card;
    }

    private void setStars(int score, ImageView star1, ImageView star2, ImageView star3, ImageView star4, ImageView star5)
    {
        switch (score)
        {
            case 0:
                star1.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star2.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star3.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star4.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star5.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                break;
            case 1:
                star1.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star2.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star3.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star4.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star5.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                break;
            case 2:
                star1.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star2.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star3.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star4.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star5.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                break;
            case 3:
                star1.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star2.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star3.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star4.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star5.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                break;
            case 4:
                star1.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star2.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star3.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star4.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star5.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                break;
            case 5:
                star1.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star2.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star3.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star4.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                star5.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_grey600_38dp));
                break;
            default:
                star1.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star2.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star3.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star4.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                star5.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_outline_grey600_38dp));
                break;

        }
    }

    static class ViewHolderItem
    {
        TextView txtReviewName;
        TextView txtReview;
        TextView txtReviewDate;

        public ViewHolderItem(View card)
        {
            this.txtReviewName = (TextView) card.findViewById(R.id.txbBeoordelingNaam);
            this.txtReview = (TextView) card.findViewById(R.id.txbBeoordeling);
            this.txtReviewDate = (TextView) card.findViewById(R.id.txbBeoordelingDate);
        }
    }

}
