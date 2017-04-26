package company.chohee.csc780;

import android.os.AsyncTask;
import android.util.Log;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;

import java.io.IOException;

import company.chohee.csc780.model.Restaurant;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Chohee on 4/17/17.
 */

public class YelpBusinessService extends AsyncTask<String, Void, Restaurant>{

    private static final String TAG = YelpService.class.getSimpleName();
    private static final String TOKEN = BuildConfig.TOKEN;
    YelpFusionApiFactory apiFactory = new YelpFusionApiFactory();
    YelpFusionApi yelpFusionApi = null;

    @Override
    protected Restaurant doInBackground(String... strings) {
        try {
            yelpFusionApi = apiFactory.createAPI(TOKEN);
        } catch (IOException e) {
            e.printStackTrace();
        }



        Call<Business> call = yelpFusionApi.getBusiness(strings[0]);

        Response<Business> response;

        try {
            response = call.execute();
            Business business = response.body();

            Restaurant restaurant = new Restaurant(business.getId(), business.getName(),
                    business.getImageUrl(), business.getPrice(), business.getPhone(),business.getCategories().get(0).getAlias(), business.getReviewCount()
                    ,business.getRating(), business.getLocation(), business.getHours(), business.getPhotos());
            restaurant.setDistance(business.getDistance());
            restaurant.setUrl(business.getUrl());
            restaurant.setClosed(business.getIsClosed());
            restaurant.setCoordinates(business.getCoordinates());

            return restaurant;


        } catch (IOException e) {
            Log.d(TAG, "call execution not working");
            e.printStackTrace();
        }

        return null;

    }
}
