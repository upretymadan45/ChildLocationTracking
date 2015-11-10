package com.example.test.childlocationtracking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 21502476 on 4/11/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Tracker.db";

    public static final String USERS_TABLE_NAME = "users";
    public static final String USERS_COLUMN_ID = "id";
    public static final String USERS_COLUMN_NAME = "name";
    public static final String USERS_COLUMN_PASSWORD = "password";

    public static final String GEOFENCE_TABLE_NAME = "geofence";
    public static final String GEOFENCE_COLUMN_ID = "id";
    public static final String GEOFENCE_COLUMN_ADDRESS = "address";
    public static final String GEOFENCE_COLUMN_LAT = "latitude";
    public static final String GEOFENCE_COLUMN_LONG = "longitude";
    public static final String GEOFENCE_COLUMN_RADIUS = "radius";

    public static final String LOCATIONINFO_TABLE_NAME = "locationInfo";
    public static final String LOCATIONINFO_COLUMN_ID = "id";
    public static final String LOCATIONINFO_COLUMN_LATITUDE = "latitude";
    public static final String LOCATIONINFO_COLUMN_LONGITUDE = "longitude";
    public static final String LOCATIONINFO_COLUMN_SPEED="speed";

    private HashMap hp;

    public DBHelper(Context context){
        //super(context, DATABASE_NAME , null, 1);
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table "+USERS_TABLE_NAME+" ("+USERS_COLUMN_ID+" integer primary key AUTOINCREMENT, " +
                                                        ""+USERS_COLUMN_NAME+" text, " +
                                                        ""+USERS_COLUMN_PASSWORD+" text)"
        );

        db.execSQL(
                "create table " + GEOFENCE_TABLE_NAME + " (" + GEOFENCE_COLUMN_ID + " integer primary key AUTOINCREMENT, " +
                        "" + GEOFENCE_COLUMN_ADDRESS + " text, " +
                        "" + GEOFENCE_COLUMN_LAT + " text, " +
                        "" + GEOFENCE_COLUMN_LONG + " text, " +
                        "" + GEOFENCE_COLUMN_RADIUS + " text)"
        );

        db.execSQL(
                "create table " + LOCATIONINFO_TABLE_NAME + " (" + LOCATIONINFO_COLUMN_ID + " integer primary key AUTOINCREMENT, " +
                        "" + LOCATIONINFO_COLUMN_LATITUDE + " text, " +
                        "" + LOCATIONINFO_COLUMN_LONGITUDE + " text," +
                        "" + LOCATIONINFO_COLUMN_SPEED + " text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + USERS_TABLE_NAME + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + GEOFENCE_TABLE_NAME + "'");
        onCreate(db);
    }

    public boolean createUser  (String username, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COLUMN_NAME, username);
        contentValues.put(USERS_COLUMN_PASSWORD, password);
        db.insert(USERS_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean addGeofence  (String address,String latitude,String longitude, String radius)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GEOFENCE_COLUMN_ADDRESS, address);
        contentValues.put(GEOFENCE_COLUMN_LAT, latitude);
        contentValues.put(GEOFENCE_COLUMN_LONG,longitude);
        contentValues.put(GEOFENCE_COLUMN_RADIUS, radius);
        db.insert(GEOFENCE_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean addLocationInfo(String latitude,String longitude,String speed)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOCATIONINFO_COLUMN_LATITUDE, latitude);
        contentValues.put(LOCATIONINFO_COLUMN_LONGITUDE, longitude);
        contentValues.put(LOCATIONINFO_COLUMN_SPEED,speed);
        db.delete(LOCATIONINFO_TABLE_NAME,null,null);
        db.insert(LOCATIONINFO_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from '"+USERS_TABLE_NAME+"' where '"+USERS_COLUMN_NAME+"'="+username+"", null );
        return res;
    }

    public List<GetGeofenceDataFromDB> getAllCotacts()
    {
        List<GetGeofenceDataFromDB> geofence_list = new ArrayList<GetGeofenceDataFromDB>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from '"+GEOFENCE_TABLE_NAME+"'", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            int id=res.getInt(res.getColumnIndex(GEOFENCE_COLUMN_ID));
            String address=res.getString(res.getColumnIndex(GEOFENCE_COLUMN_ADDRESS));
            String latitude = res.getString(res.getColumnIndex(GEOFENCE_COLUMN_LAT));
            String longitude=res.getString(res.getColumnIndex(GEOFENCE_COLUMN_LONG));
            String radius=res.getString(res.getColumnIndex(GEOFENCE_COLUMN_RADIUS));
            geofence_list.add(new GetGeofenceDataFromDB(id,address,latitude,longitude,radius));
            res.moveToNext();
        }
        return geofence_list;
    }
}
