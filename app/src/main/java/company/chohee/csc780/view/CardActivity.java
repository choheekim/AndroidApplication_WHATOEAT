package company.chohee.csc780.view;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import company.chohee.csc780.R;
import company.chohee.csc780.YelpService;
import company.chohee.csc780.adapter.SimpleCardStackAdapter;
import company.chohee.csc780.model.Orientations;
import company.chohee.csc780.model.Restaurant;

public class CardActivity extends AppCompatActivity {

    private static final String TAG = CardActivity.class.getSimpleName();
    private CardContainer mCardContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setContentView(R.layout.activity_card);


        mCardContainer = (CardContainer) findViewById(R.id.card_container_view);

        mCardContainer.setOrientation(company.chohee.csc780.model.Orientations.Orientation.Ordered);
        mCardContainer.setOrientation(Orientations.Orientation.Disordered);

        List<Restaurant> result = null;

        Intent intent = getIntent();
        Double latitude = intent.getDoubleExtra("latitude", 37.7749);
        Double longitude = intent.getDoubleExtra("longitude", -122.393990);

        boolean[] priceRange = intent.getBooleanArrayExtra("priceToggleBoolean");
        boolean[] radiusRange = intent.getBooleanArrayExtra("radiusToggleBoolean");

        String priceParameter = getPriceParameter(priceRange);
        String radiusParameter = getRadiusParameter(radiusRange);
        String term = intent.getStringExtra("term");



        try {
            result = new YelpService().execute
                    (Double.toString(latitude), Double.toString(longitude),
                            term, priceParameter, radiusParameter).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        Resources r = getResources();

        final SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(this);

        for(Restaurant restaurant : result) {
            adapter.add(restaurant);
        }


        mCardContainer.setAdapter(adapter);

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

    private String getRadiusParameter(boolean[] arr) {

        int optionIndex = -1;
        String result = null;
        for(int i = 0; i < arr.length ; i++) {
            if(arr[i])
                optionIndex = i;
        }


        switch (optionIndex) {
            case 0:
                result = "8000";
                break;
            case 1:
                result = "16000";
                break;
            case 2:
                result = "25000";
                break;
            case 3:
                result = "32186";
                break;
            case 4:
                result = "40000";
                break;
        }
        return result;

    }

    private String getPriceParameter(boolean[] arr) {

        StringBuffer price;

        if(trueIndex(arr).size() != 0) {
            List<Integer> priceIndex = trueIndex(arr);

            price = new StringBuffer();
            for(Integer idx : priceIndex) {
                price.append(Integer.toString(idx));
                price.append(",");
            }

            return price.substring(0, price.length()-1);

        }

        return null;
    }

    private List<Integer> trueIndex (boolean[] arr) {

        List<Integer> result = new ArrayList<>();

        for(int i = 0; i < arr.length; i++) {
            if(arr[i])
                result.add(i+1);
        }

        return result;
    }
}
