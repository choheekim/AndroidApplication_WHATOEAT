package company.chohee.csc780.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import company.chohee.csc780.R;
import company.chohee.csc780.model.Restaurant;
import company.chohee.csc780.view.DetailActivity;

/**
 * Created by Chohee on 4/17/17.
 */

public class SimpleCardStackAdapter extends CardStackAdapter {

    public final static String TAG = SimpleCardStackAdapter.class.getSimpleName();

    Context mContext;

    public SimpleCardStackAdapter(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    @Override
    protected View getCardView(int position, Restaurant model, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.std_card_inner, parent, false);
            assert convertView != null;
        }


        Double rating = model.getRating();
        setRatingImageFile(rating, convertView);
        final Restaurant finalRestaurant = model;


        ((TextView) convertView.findViewById(R.id.title)).setText(model.getName());
        ((TextView) convertView.findViewById(R.id.rating_text)).setText(Double.toString(model.getRating()));
        ((TextView) convertView.findViewById(R.id.number_of_reviews)).setText(Integer.toString(model.getReviewCount()) + " reviews");
        ((TextView) convertView.findViewById(R.id.price)).setText(model.getPrice() + " ·");
        ((TextView) convertView.findViewById(R.id.category_text)).setText(model.getCategory());

        ImageView imageView = ((ImageView) convertView.findViewById(R.id.image));
        Glide.with
                (convertView.getContext())
                .load(model.getImageUrl())
                .fitCenter()
                .into(imageView);

        model.setOnCardDismissedListener(new test(model.getId()));

        Button mCallButton = (Button) convertView.findViewById(R.id.call_button_card);
        Button mShareButton = (Button) convertView.findViewById(R.id.share_button_card);

        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, finalRestaurant.getPhone());
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + finalRestaurant.getPhone()));
                if (ActivityCompat.checkSelfPermission(mContext , Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mContext.startActivity(intent);
            }
        });


        final String textMessage = "Hey, check out the restaurant. \n" + model.getUrl();

        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
                sendIntent.setType("text/plain");
                mContext.startActivity(sendIntent);
            }
        });




        return convertView;
    }

    private void setRatingImageFile(Double rating, View convertView) {

        ImageView rating_image_view = (ImageView) convertView.findViewById(R.id.rating_starts);

        if(rating == 0.0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rating_image_view.setImageDrawable(mContext.getDrawable(R.drawable.star_0_0));
            }else {
                rating_image_view.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star_0_0));
            }

        }else if(rating == 0.5){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rating_image_view.setImageDrawable(mContext.getDrawable(R.drawable.star_0_5));
            }else {
                rating_image_view.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star_0_5));
            }
        }else if(rating == 1.0){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rating_image_view.setImageDrawable(mContext.getDrawable(R.drawable.star_1_0));
            }else {
                rating_image_view.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star_1_0));
            }
        }else if(rating == 1.5){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rating_image_view.setImageDrawable(mContext.getDrawable(R.drawable.star_1_5));
            }else {
                rating_image_view.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star_1_5));
            }
        }else if(rating == 2.0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rating_image_view.setImageDrawable(mContext.getDrawable(R.drawable.star_2_0));
            }else {
                rating_image_view.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star_2_0));
            }
        }else if(rating == 2.5) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rating_image_view.setImageDrawable(mContext.getDrawable(R.drawable.star_2_5));
            }else {
                rating_image_view.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star_2_5));
            }
        }else if(rating == 3.0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rating_image_view.setImageDrawable(mContext.getDrawable(R.drawable.star_3_0));
            }else {
                rating_image_view.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star_3_0));
            }
        }else if(rating == 3.5){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rating_image_view.setImageDrawable(mContext.getDrawable(R.drawable.star_3_5));
            }else {
                rating_image_view.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star_3_5));
            }
        }else if(rating == 4.0){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rating_image_view.setImageDrawable(mContext.getDrawable(R.drawable.star_4_0));
            }else {
                rating_image_view.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star_4_0));
            }
        }else if(rating == 4.5){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rating_image_view.setImageDrawable(mContext.getDrawable(R.drawable.star_4_5));
            }else {
                rating_image_view.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star_4_5));
            }
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rating_image_view.setImageDrawable(mContext.getDrawable(R.drawable.star_5_0));
            }else {
                rating_image_view.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star_5_0));
            }
        }

    }

    class test implements Restaurant.OnCardDismissedListener {

        String id;

        public test(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public void onLike() {

            Log.d(TAG, id);
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra("restaurantId", id);
            mContext.startActivity(intent);
        }

        @Override
        public void onDislike() {

            Log.i("Swipeable Cards","I dislike the card");

        }
    }

}
