package company.chohee.csc780.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yelp.fusion.client.models.Review;

import java.util.ArrayList;

import company.chohee.csc780.R;

/**
 * Created by Chohee on 4/25/17.
 */

public class ReviewAdapter extends
        RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private ArrayList<Review> mReviews;
    private Context mContext;

    public ReviewAdapter(ArrayList<Review> reviews, Context context) {
        mReviews = reviews;
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View reviewView = inflater.inflate(R.layout.item_review, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(reviewView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        // Get the data model based on position
        final Review review = mReviews.get(position);

        // Set item views based on your views and data model
        holder.userNameView.setText(review.getUser().getName());
        holder.reviewDateView.setText(review.getTimeCreated());

        TextView reviewTextView = holder.reviewTextView;
        reviewTextView.setText(review.getText());
        reviewTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl()));
                mContext.startActivity(intent);
            }
        });
        setRatingImageFile(review.getRating(), holder);
    }

    private void setRatingImageFile(int rating, ViewHolder holder) {

        if(rating == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.reviewImageView.setImageDrawable(mContext.getDrawable(R.drawable.star_0_0));
            }else {
                holder.reviewImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star_0_0));
            }

        }else if(rating == 1){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.reviewImageView.setImageDrawable(mContext.getDrawable(R.drawable.star_1_0));
            }else {
                holder.reviewImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star_1_0));
            }
        }else if(rating == 2) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.reviewImageView.setImageDrawable(mContext.getDrawable(R.drawable.star_2_0));
            }else {
                holder.reviewImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star_2_0));
            }
        }else if(rating == 3) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.reviewImageView.setImageDrawable(mContext.getDrawable(R.drawable.star_3_0));
            }else {
                holder.reviewImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star_3_0));
            }
        }else if(rating == 4){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.reviewImageView.setImageDrawable(mContext.getDrawable(R.drawable.star_4_0));
            }else {
                holder.reviewImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star_4_0));
            }
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.reviewImageView.setImageDrawable(mContext.getDrawable(R.drawable.star_5_0));
            }else {
                holder.reviewImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star_5_0));
            }
        }

    }


    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView userNameView;
        public TextView reviewDateView;
        public TextView reviewTextView;
        public ImageView reviewImageView;


        public ViewHolder(View itemView) {
            super(itemView);

            userNameView = (TextView) itemView.findViewById(R.id.review_user_name);
            reviewDateView = (TextView) itemView.findViewById(R.id.review_date);
            reviewTextView = (TextView) itemView.findViewById(R.id.review_text);
            reviewImageView = (ImageView) itemView.findViewById(R.id.review_image_rating);
        }
    }
}
