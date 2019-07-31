package com.example.borja.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SqlHelper extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "GetThereDB";

    // Places table name
    private static final String TABLE_PLACES = "places";

    // Places Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LONG = "long";
    private static final String KEY_TYPE = "type";


    public SqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create places table
        String CREATE_PLACES_TABLE = "CREATE TABLE places ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "address TEXT, " +
                "lat TEXT, " +
                "long TEXT," +
                "type TEXT )";

        // create places table
        db.execSQL(CREATE_PLACES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addPlace(Address place) {
        Log.d("addplace", place.toString());


        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ADDRESS, place.getAddress()); // get address
        values.put(KEY_LONG, place.getLog()); // get log
        values.put(KEY_LAT, place.getLat()); //get lat
        values.put(KEY_TYPE, place.getType()); //get type

        // 3. insert
        db.insert(TABLE_PLACES, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/values

        // 4. Close dbase
        db.close();
    }


    public int getIds() {
        String selectQuery = "SELECT id FROM places";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor c = database.rawQuery(selectQuery, null);
        c.moveToFirst();
        int total = c.getCount();

        return total;
    }

    public String getHome() {
        StringBuilder s = new StringBuilder();
        String home = "home";
        String selectQuery = "SELECT address,type FROM places WHERE type ='" + home + "' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                s.append(cursor.getString(0));  //retrieve title
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return s.toString();
    }

    public void deletePlace(String type){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLACES, KEY_TYPE + "=?", new String[]{type});
    }

    public String getWork() {
        StringBuilder s = new StringBuilder();
        String work = "work";
        String selectQuery = "SELECT address,type FROM places WHERE type ='" + work + "' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                s.append(cursor.getString(0));  //retrieve title
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return s.toString();
    }


    public String getlong(String address) {
        StringBuilder s = new StringBuilder();
        String selectQuery = "SELECT long FROM places WHERE type ='" + address + "' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                s.append(cursor.getString(0));  //retrieve title
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return s.toString();


    }

    public String getlat(String address) {
        StringBuilder s = new StringBuilder();
        String selectQuery = "SELECT lat FROM places WHERE type ='" + address + "' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                s.append(cursor.getString(0));  //retrieve title
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return s.toString();
    }
}