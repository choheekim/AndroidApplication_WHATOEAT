package company.chohee.csc780;

import android.os.AsyncTask;
import android.util.Log;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import company.chohee.csc780.model.Restaurant;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Chohee on 4/16/17.
 */

public class YelpService extends AsyncTask<String, Void, ArrayList<Restaurant>> {

    private static final String TAG = YelpService.class.getSimpleName();
    private static final String TOKEN = BuildConfig.TOKEN;
    YelpFusionApiFactory apiFactory = new YelpFusionApiFactory();
    YelpFusionApi yelpFusionApi = null;

    @Override
    protected ArrayList<Restaurant> doInBackground(String... strings) {

        try {
            yelpFusionApi = apiFactory.createAPI(TOKEN);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Map<String, String> params = new HashMap<>();

        // general params
        params.put("latitude", strings[0]);
        params.put("longitude", strings[1]);

        if(strings[2] != null) {
            params.put("term", strings[2]);
        }

        if(strings[3] != null) {
            params.put("price", strings[3]);
        }

        if(strings[4] != null) {
            params.put("radius", strings[4]);
        }
        params.put("categories", "restaurants");
        params.put("limit", "50");

        Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);

        Response<SearchResponse> response;
        ArrayList<Restaurant> restaurants = new ArrayList<>();

        try {
            response = call.execute();
            SearchResponse body = response.body();

            for (Business business : body.getBusinesses()) {
                Log.d(TAG, business.getId());

                    Restaurant restaurant = new Restaurant(business.getId(), business.getName(),
                            business.getImageUrl(), business.getPrice(), business.getPhone(), business.getCategories().get(0).getAlias(), business.getReviewCount(),
                            business.getRating(), business.getLocation(), business.getHours(), business.getPhotos());
                    restaurant.setUrl(business.getUrl());

                    restaurants.add(restaurant);

            }

        } catch (IOException e) {
            Log.d(TAG, "call execution not working");
            e.printStackTrace();
        }

        return restaurants;
    }
}
