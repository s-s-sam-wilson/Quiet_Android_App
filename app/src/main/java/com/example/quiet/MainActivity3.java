package com.example.quiet;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity3 extends AppCompatActivity {
    private final static int PLACE_PICKER_REQUEST = 1;
    TextView tv1;
    TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main3);
        tv1 = findViewById(R.id.textView2);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        ListView listView = findViewById(R.id.list_view);
        DatabaseHelper db = new DatabaseHelper(this);
        List<LatLng> ll = db.GetLoc();
        List<String> data = new ArrayList<>();
        Iterator<LatLng> iterator = ll.iterator();
        while(iterator.hasNext()){
            LatLng l1 = iterator.next();
            data.add("Latitude: "+l1.latitude+"\nLongitude: "+l1.longitude);
        }

        CustomAdapter adapter = new CustomAdapter(this, data);
        listView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity3.this, MapsActivity.class);
                startActivityForResult(intent1, 1);
            }
        });
    }
    @Override
    public void onBackPressed() {
        // Finish the current activity
        finish();

        // Create an Intent to start the new activity
        Intent intent = new Intent(this, MainActivity2.class);

        // Start the new activity
        startActivity(intent);
    }
}