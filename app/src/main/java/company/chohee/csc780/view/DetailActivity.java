package company.chohee.csc780.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.yelp.fusion.client.models.Location;
import com.yelp.fusion.client.models.Review;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import company.chohee.csc780.R;
import company.chohee.csc780.YelpBusinessService;
import company.chohee.csc780.YelpReviewService;
import company.chohee.csc780.adapter.ReviewAdapter;
import company.chohee.csc780.model.Restaurant;

public class DetailActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private SliderLayout mDemoSlider;
    private RecyclerView mRecyclerView;
    private ImageButton mCallButton;
    private ImageButton mWebsiteButton;
    private ImageButton mShareButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDemoSlider = (SliderLayout) findViewById(R.id.slider);

        mCallButton = (ImageButton) findViewById(R.id.call_image_button);
        mWebsiteButton = (ImageButton) findViewById(R.id.website_image_button);
        mShareButton = (ImageButton) findViewById(R.id.share_image_button);


        mRecyclerView = (RecyclerView) findViewById(R.id.review_recycler_view);
        Intent intent = getIntent();
        String restaurantId = intent.getStringExtra("restaurantId");


        Restaurant restaurant = null;
        ArrayList<Review> reviews = null;

        try {
            restaurant = new YelpBusinessService().execute
                    (restaurantId).get();
            reviews = new YelpReviewService().execute(restaurantId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        final Restaurant finalRestaurant = restaurant;


        ReviewAdapter reviewAdapter = new ReviewAdapter(reviews, this);
        mRecyclerView.setAdapter(reviewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        final String restaurantName = restaurant.getName();
        Double restaurantRating = restaurant.getRating();
        Integer restaurantReview = restaurant.getReviewCount();
        String restaurantPrice = restaurant.getPrice();
        Location restaurantAddress = restaurant.getLocation();
        final Double latitude = restaurant.getCoordinates().getLatitude();
        final Double longitude = restaurant.getCoordinates().getLongitude();
        ArrayList<String> photos = restaurant.getPhotos();

        final String textMessage = "Hey, check out the restaurant. \n" + restaurant.getUrl();

        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, finalRestaurant.getPhone());
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + finalRestaurant.getPhone()));
                if (ActivityCompat.checkSelfPermission(DetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });

        mWebsiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalRestaurant.getUrl()));
                startActivity(intent);
            }
        });

        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        //set photos
        for(String photo : photos){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .image(photo)
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        TextView addressText = (TextView)findViewById(R.id.address_text_view);
        addressText.setText(getAddress(restaurantAddress.getDisplayAddress()));
        addressText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(isGoogleMapsInstalled()) {

                    Uri gmmIntentUri = Uri.parse("geo:" + Double.toString(latitude) + "," + Double.toString(longitude)
                            + "?q=" + Uri.encode(restaurantName));

                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }else{
                    Toast.makeText(DetailActivity.this, "Google map installation required", Toast.LENGTH_SHORT);
                }
            }
        });

        ((TextView)findViewById(R.id.restaurant_name_detail)).setText(restaurantName);
        ((TextView)findViewById(R.id.rating_text_detail)).setText(Double.toString(restaurantRating));
        ((TextView)findViewById(R.id.reviews_and_price_detail_view)).setText(Integer.toString(restaurantReview) + " reviews · "+restaurantPrice);
        ((TextView)findViewById(R.id.hour_text_view)).setText(getOpenText(restaurant.isClosed()));

        //set rating image
        setRatingImageFile(restaurantRating);

        ImageButton iv = (ImageButton)findViewById(R.id.home_button);
        iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Called when your View is clicked
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });


    }

    public boolean isGoogleMapsInstalled()
    {
        try
        {
            ApplicationInfo info = getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
            return true;
        }
        catch(PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

    private String getOpenText(boolean isClosed) {
        if(isClosed)
            return "Closed";
        else
            return "Open today";
    }

    private String getAddress(ArrayList<String> address) {

        StringBuffer completeAddress = new StringBuffer();
        for(String piece : address) {
            completeAddress.append(piece);
            completeAddress.append(" ");
        }

        return completeAddress.toString();
    }

    private void setRatingImageFile(Double rating) {

        ImageView rating_image_view = (ImageView)findViewById(R.id.rating_image_view_detail);

        if(rating == 0.0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rating_image_view.setImageDrawable(this.getDrawable(R.drawable.star_0_0));
            }else {
                rating_image_view.setImageDrawable(this.getResources().getDrawable(R.drawable.star_0_0));
            }

        }else if(rating == 0.5){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rating_image_view.setImageDrawable(this.getDrawable(R.drawable.star_0_5));
            }else {
                rating_image_view.setImageDrawable(this.getResources().getDrawable(R.drawable.star_0_5));
            }
        }else if(rating == 1.0){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rating_image_view.setImageDrawable(this.getDrawable(R.drawable.star_1_0));
            }else {
                rating_image_view.setImageDrawable(this.getResources().getDrawable(R.drawable.star_1_0));
            }
        }else if(rating == 1.5){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rating_image_view.setImageDrawable(this.getDrawable(R.drawable.star_1_5));
            }else {
                rating_image_view.setImageDrawable(this.getResources().getDrawable(R.drawable.star_1_5));
            }
        }else if(rating == 2.0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rating_image_view.setImageDrawable(this.getDrawable(R.drawable.star_2_0));
            }else {
                rating_image_view.setImageDrawable(this.getResources().getDrawable(R.drawable.star_2_0));
            }
        }else if(rating == 2.5) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rating_image_view.setImageDrawable(this.getDrawable(R.drawable.star_2_5));
            }else {
                rating_image_view.setImageDrawable(this.getResources().getDrawable(R.drawable.star_2_5));
            }
        }else if(rating == 3.0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rating_image_view.setImageDrawable(this.getDrawable(R.drawable.star_3_0));
            }else {
                rating_image_view.setImageDrawable(this.getResources().getDrawable(R.drawable.star_3_0));
            }
        }else if(rating == 3.5){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rating_image_view.setImageDrawable(this.getDrawable(R.drawable.star_3_5));
            }else {
                rating_image_view.setImageDrawable(this.getResources().getDrawable(R.drawable.star_3_5));
            }
        }else if(rating == 4.0){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rating_image_view.setImageDrawable(this.getDrawable(R.drawable.star_4_0));
            }else {
                rating_image_view.setImageDrawable(this.getResources().getDrawable(R.drawable.star_4_0));
            }
        }else if(rating == 4.5){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rating_image_view.setImageDrawable(this.getDrawable(R.drawable.star_4_5));
            }else {
                rating_image_view.setImageDrawable(this.getResources().getDrawable(R.drawable.star_4_5));
            }
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rating_image_view.setImageDrawable(this.getDrawable(R.drawable.star_5_0));
            }else {
                rating_image_view.setImageDrawable(this.getResources().getDrawable(R.drawable.star_5_0));
            }
        }

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
