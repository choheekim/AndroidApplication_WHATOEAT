package company.chohee.csc780;

import android.os.AsyncTask;
import android.util.Log;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Review;
import com.yelp.fusion.client.models.Reviews;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Chohee on 4/25/17.
 */

public class YelpReviewService extends AsyncTask<String, Void, ArrayList<Review>> {

    private static final String TAG = YelpReviewService.class.getSimpleName();
    private static final String TOKEN = BuildConfig.TOKEN;
    YelpFusionApiFactory apiFactory = new YelpFusionApiFactory();
    YelpFusionApi yelpFusionApi = null;

    @Override
    protected ArrayList<Review> doInBackground(String... strings) {

        try {
            yelpFusionApi = apiFactory.createAPI(TOKEN);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String restaurant_id = strings[0];
        String locale = "en_US";

        Call<Reviews> call = yelpFusionApi.getBusinessReviews(restaurant_id, locale);

        Response<Reviews> response;
        ArrayList<Review> reviews = new ArrayList<>();

        try {
            response = call.execute();
            Reviews body = response.body();

            Log.d(TAG, Integer.toString(body.getTotal()));
            Log.d(TAG, Integer.toString(body.getReviews().size()));

            for (Review cur_review : body.getReviews()) {
                Log.d(TAG, cur_review.getUser().getName());
                reviews.add(cur_review);
            }

        } catch (IOException e) {
            Log.d(TAG, "call execution not working");
            e.printStackTrace();
        }

        return reviews;
    }
}
