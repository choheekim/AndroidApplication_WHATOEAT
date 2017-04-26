package company.chohee.csc780.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;

import company.chohee.csc780.R;
import company.chohee.csc780.TrackGPS;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();

    protected double latitude;
    protected double longitude;
    private TrackGPS gps;

    private MultiStateToggleButton mPriceToggleButton;
    private MultiStateToggleButton mRadiusToggleButton;
    private EditText mTermEditText;
    private Button mGoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        mPriceToggleButton = (MultiStateToggleButton) findViewById(R.id.price_toggle_button);
        mPriceToggleButton.enableMultipleChoice(true);

        mRadiusToggleButton = (MultiStateToggleButton) findViewById(R.id.radious_toggle_button);
        mTermEditText = (EditText) findViewById(R.id.term);

        gps = new TrackGPS(MainActivity.this);

        if(gps.canGetLocation()){

            longitude = gps.getLongitude();
            latitude = gps .getLatitude();
        }else {
            gps.showSettingsAlert();
        }

        mGoButton = (Button) findViewById(R.id.go_button);
        mGoButton.setOnClickListener(new View.OnClickListener() {

            Intent intent;
            @Override
            public void onClick(View view) {

                intent = new Intent(view.getContext(), CardActivity.class);
                intent.putExtra("longitude", longitude);
                intent.putExtra("latitude", latitude);
                intent.putExtra("term", mTermEditText.getText().toString());
                intent.putExtra("priceToggleBoolean",mPriceToggleButton.getStates() );
                intent.putExtra("radiusToggleBoolean", mRadiusToggleButton.getStates());


                startActivity(intent);
            }
        });

        ImageButton iv = (ImageButton)findViewById(R.id.home_button);
        iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Called when your View is clicked
                Log.d(TAG, "Home button is clicked");
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }



}
