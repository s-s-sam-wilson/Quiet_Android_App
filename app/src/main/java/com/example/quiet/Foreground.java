package com.example.quiet;

import static java.security.AccessController.getContext;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.Iterator;
import java.util.List;

public class Foreground extends Service {
    DatabaseHelper db;
    AudioManager am;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(0);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        String CHANNEL_ID = "Foreground service";
        db = new DatabaseHelper(this);
        //Notification
        NotificationChannel notificationChannel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_ID,
                NotificationManager.IMPORTANCE_LOW
        );
        Toast toast = Toast.makeText(this,"Its silent",Toast.LENGTH_SHORT);
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        getSystemService(NotificationManager.class).createNotificationChannel(notificationChannel);
        Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Quiet")
                .setContentText("Starting the Service")
                .setSmallIcon(R.drawable.colorful_iconic_photography_logo_1__removebg_preview);
        startForeground(1,notification.build());
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try{
                                fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                                        new LocationCallback() {
                                            @Override
                                            public void onLocationResult(LocationResult locationResult) {
                                                Context context = getApplicationContext();

                                                // Get the current location
                                                Location location = locationResult.getLastLocation();
                                                List<LatLng> ll = db.GetLoc();
                                                Iterator<LatLng> iterator = ll.iterator();
                                                if (location != null) {
                                                    // Get the latitude and longitude of the location
                                                    double latitude = location.getLatitude();
                                                    double longitude = location.getLongitude();
                                                    LatLng cl = new LatLng(latitude,longitude);
                                                    int flag =0;
                                                    while(iterator.hasNext()){
                                                        LatLng chl = iterator.next();
                                                        float[] results = new float[1];
                                                        Location.distanceBetween(cl.latitude, cl.longitude, chl.latitude, chl.longitude, results);

                                                        float distance = results[0];
                                                        flag=0;
                                                        if(distance<50){
                                                            toast.show();
                                                            if(am.getStreamVolume(AudioManager.STREAM_NOTIFICATION)!=0)
                                                                am.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,AudioManager.ADJUST_MUTE, 0);
                                                            if(am.getStreamVolume(AudioManager.STREAM_RING)!=0)
                                                                am.adjustStreamVolume(AudioManager.STREAM_RING,AudioManager.ADJUST_MUTE, 0);
                                                            if(am.getStreamVolume(AudioManager.STREAM_ALARM)!=0)
                                                                am.adjustStreamVolume(AudioManager.STREAM_ALARM,AudioManager.ADJUST_MUTE, 0);
                                                            flag=1;
                                                            break;
                                                        }
                                                    }
                                                    if(flag==0){
                                                        if(am.getStreamVolume(AudioManager.STREAM_RING)==0)
                                                            am.adjustStreamVolume(AudioManager.STREAM_RING,AudioManager.ADJUST_UNMUTE, 0);
                                                        if(am.getStreamVolume(AudioManager.STREAM_NOTIFICATION)==0)
                                                            am.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,AudioManager.ADJUST_UNMUTE, 0);
                                                        if(am.getStreamVolume(AudioManager.STREAM_ALARM)==0)
                                                            am.adjustStreamVolume(AudioManager.STREAM_ALARM,AudioManager.ADJUST_UNMUTE, 0);
                                                    }

                                                    CharSequence text = "Latitude: " + latitude + "\nLongitude: " + longitude;

                                                    notification.setContentText(text);
                                                    startForeground(1,notification.build());
                                                }
                                            }
                                        }, Looper.getMainLooper());
                            }catch (SecurityException se) {
                                se.printStackTrace();
                            }
                        }
                }
        ).start();
        return START_STICKY;
    }
}
