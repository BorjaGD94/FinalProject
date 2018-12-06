package com.example.borja.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SqlHelper extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "GetThereDB";

    // Books table name
    private static final String TABLE_PLACES = "places";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_ADRESS = "adress";
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
                "adress TEXT, " +
                "lat TEXT, " +
                "long TEXT," +
                "type TEXT )";

        // create books table
        db.execSQL(CREATE_PLACES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addPlace(Adress place) {
        Log.d("addplace", place.toString());


        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ADRESS, place.getAdress()); // get adress
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

    /* // Get All Books
     public List<place> getAllPlaces() {
         List<Place> books = new LinkedList<Book>();
 
         // 1. build the query
         String query = "SELECT  * FROM " + TABLE_BOOKS;
 
         // 2. get reference to writable DB
         SQLiteDatabase db = this.getWritableDatabase();
         Cursor cursor = db.rawQuery(query, null);
 
         // 3. go over each row, build book and add it to list
         Book book = null;
 
 
 
         if (cursor.moveToFirst()) {
             do {
                 book = new Book();
                 book.setId(Integer.parseInt(cursor.getString(0)));
                 book.setTitle(cursor.getString(1));
                 book.setAuthor(cursor.getString(2));
                 book.setRating(cursor.getString(3));
                 book.setImageName(cursor.getString(4));
 
                 // Add book to books
                 books.add(book);
             } while (cursor.moveToNext());
         }
 
 
 
         Log.d("getAllBooks()", books.toString());
 
         return books; // return books
     }
     // Updating single book
     public int updateBook(Book book, String newTitle, String newAuthor) {
 
         // 1. get reference to writable DB
         SQLiteDatabase db = this.getWritableDatabase();
 
         // 2. create ContentValues to add key "column"/value
         ContentValues values = new ContentValues();
         values.put("title", newTitle);
         values.put("author", newAuthor);
 
 
         // 3. updating row
         int i = db.update(TABLE_BOOKS, //table
                 values, // column/value
                 KEY_ID+" = ?", // selections
                 new String[] { String.valueOf(book.getId()) }); //selection args
         // 4. close dbase
         db.close();
         Log.d("UpdateBook", book.toString());
         return i;
 
     }
 */
    // Deleting single book
    public void deleteBook(Adress place) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_PLACES, KEY_ADRESS + " = ?", new String[]{String.valueOf(place.getAdress())});

        // 3. close
        db.close();

        Log.d("deleteBook", place.toString());
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
        String selectQuery = "SELECT adress,type FROM places WHERE type ='" + home + "' ";
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

    public String getWork() {
        StringBuilder s = new StringBuilder();
        String work = "work";
        String selectQuery = "SELECT adress,type FROM places WHERE type ='" + work + "' ";
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


    public String getlong(String adress) {
        StringBuilder s = new StringBuilder();
        String selectQuery = "SELECT long FROM places WHERE type ='" + adress + "' ";
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

    public String getlat(String adress) {
        StringBuilder s = new StringBuilder();
        String selectQuery = "SELECT lat FROM places WHERE type ='" + adress + "' ";
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