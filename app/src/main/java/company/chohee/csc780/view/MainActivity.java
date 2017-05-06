package company.chohee.csc780.view;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;

import company.chohee.csc780.R;
import company.chohee.csc780.TrackGPS;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();

    static final Integer LOCATION = 0x1;

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

        this.registerReceiver(this.mConnReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        mPriceToggleButton = (MultiStateToggleButton) findViewById(R.id.price_toggle_button);
        mPriceToggleButton.enableMultipleChoice(true);

        mRadiusToggleButton = (MultiStateToggleButton) findViewById(R.id.radious_toggle_button);
        mTermEditText = (EditText) findViewById(R.id.term);

        gps = new TrackGPS(MainActivity.this);

        if(gps.canGetLocation()){

            longitude = gps.getLongitude() == 0.0? 37.615223 : gps.getLongitude();
            latitude = gps .getLatitude()== 0.0? -122.389977 : gps.getLatitude();
        }else {
            gps.showSettingsAlert();
        }

        Log.d(TAG, "longitude : " + longitude);
        Log.d(TAG, "latitude : " + latitude);


        mGoButton = (Button) findViewById(R.id.go_button);
        mGoButton.setEnabled(false);

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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }



    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    public void ask(View v){
        switch (v.getId()){
            case R.id.go_button:
                askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);
                break;
            default:
                break;
        }
    }

    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

            mGoButton = (Button) findViewById(R.id.go_button);

            if(currentNetworkInfo.isConnected()){
                mGoButton.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
            }else{
                mGoButton.setEnabled(false);
                Toast.makeText(getApplicationContext(), "Not Connected, please connect network", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }



}
