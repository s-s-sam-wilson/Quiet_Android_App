package com.example.quiet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class MainActivity2 extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_PERMISSIONS_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ToggleButton tb = findViewById(R.id.tb1);
        String CHANNEL_ID = "Location Updates";
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        TextView lat = findViewById(R.id.Latitude);
        TextView lon = findViewById(R.id.Longitude);

        Intent serviceIntent = new Intent(this, Foreground.class);

        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, PackageManager.PERMISSION_GRANTED);
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(am.getStreamVolume(AudioManager.STREAM_NOTIFICATION)!=0)
                        am.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,AudioManager.ADJUST_MUTE, 0);
                    if(am.getStreamVolume(AudioManager.STREAM_RING)!=0)
                        am.adjustStreamVolume(AudioManager.STREAM_RING,AudioManager.ADJUST_MUTE, 0);
                    if(am.getStreamVolume(AudioManager.STREAM_ALARM)!=0)
                        am.adjustStreamVolume(AudioManager.STREAM_ALARM,AudioManager.ADJUST_MUTE, 0);
                } else {
                        am.adjustStreamVolume(AudioManager.STREAM_RING,AudioManager.ADJUST_UNMUTE, 0);
                    if(am.getStreamVolume(AudioManager.STREAM_NOTIFICATION)==0)
                        am.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,AudioManager.ADJUST_UNMUTE, 0);
                    if(am.getStreamVolume(AudioManager.STREAM_ALARM)==0)
                        am.adjustStreamVolume(AudioManager.STREAM_ALARM,AudioManager.ADJUST_UNMUTE, 0);
                }

            }
        });
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(0);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the ACCESS_FINE_LOCATION permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            // The ACCESS_FINE_LOCATION permission is already granted, so you can request location updates
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                    new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            Context context = getApplicationContext();

                            // Get the current location
                            Location location = locationResult.getLastLocation();

                            // Check if the location is not null
                            if (location != null) {
                                // Get the latitude and longitude of the location
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                lat.setText("Latitude:" + latitude);
                                lon.setText("Longitude:" + longitude);
                            }
                        }
                    }, Looper.getMainLooper());
        }
        Button btoadd = findViewById(R.id.btoadd);
        btoadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity2.this, MainActivity3.class);
                startActivity(intent1);
            }
        });

        ToggleButton tb2 = findViewById(R.id.tb2);
        tb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startForegroundService(serviceIntent);
                } else {
                    stopService(serviceIntent);
                }

            }
        });

    }
}