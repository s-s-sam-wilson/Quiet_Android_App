package com.example.quiet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.ViewDebug;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Quiet.db";
    private static final int DATABASE_VERSION = 1;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE loc ( lat float, long float)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // handle database upgrade here
    }
    public void InsertLoc(LatLng ll){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("lat",ll.latitude);
        values.put("long",ll.longitude);
        db.insert("loc",null,values );
    }
    public List<LatLng> GetLoc(){
        SQLiteDatabase db=this.getWritableDatabase();
        String selectQuery = "SELECT * FROM loc";
        List<LatLng> data = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery,null);
        while (cursor.moveToNext()){
            data.add(new LatLng(
                    cursor.getFloat(cursor.getColumnIndexOrThrow("lat")),
                    cursor.getFloat(cursor.getColumnIndexOrThrow("long"))
            ));
        }
        return  data;
    }
    public void delete(String ss){
        SQLiteDatabase db=this.getWritableDatabase();
        Scanner sc = new Scanner(ss);
        sc.next();
        String s1 = sc.next();
        sc.next();
        String s2 = sc.next();
        ContentValues values = new ContentValues();
        String selection = "ROUND(lat,5) = ? AND ROUND(long,5) = ?";
        String[] selectionArgs = {(float)Math.round((Float.parseFloat(s1)*100000)/100000.0)+"", (float)Math.round((Float.parseFloat(s2)*100000)/100000.0)+"" };
        db.delete("loc",selection, selectionArgs);
    }
}