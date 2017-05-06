package company.chohee.csc780.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import company.chohee.csc780.R;
import company.chohee.csc780.TrackGPS;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 2000 ;
    private static final String TAG = SplashActivity.class.getSimpleName();
    private TrackGPS gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        gps = new TrackGPS(this);

        double longitude = 0.0;
        double latitude = 0.0;


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
