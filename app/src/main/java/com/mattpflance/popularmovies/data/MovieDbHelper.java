package com.mattpflance.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import com.mattpflance.popularmovies.data.MovieContract.ImagesEntry;
import com.mattpflance.popularmovies.data.MovieContract.FavouritesEntry;

/**
 * Manages local database for movies information
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "popularmovies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
//        final String SQL_CREATE_IMAGES_TABLE = "CREATE TABLE " + ImagesEntry.TABLE_NAME + " (" +
//                ImagesEntry._ID + " INTEGER PRIMARY KEY," +
//                ImagesEntry.COLUMN_POSTER_PATH + " TEXT UNIQUE NOT NULL, " +
//                " );";

        final String SQL_CREATE_FAVOURITES_TABLE = "CREATE TABLE " + FavouritesEntry.TABLE_NAME + " (" +
                FavouritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavouritesEntry.COLUMN_IMAGES_KEY + " INTEGER NOT NULL, " +
                FavouritesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_RELEASE + " TEXT NOT NULL," +
                FavouritesEntry.COLUMN_RATING + " REAL NOT NULL, " +
                FavouritesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL " + // Can be null ?
//                // Set up the location column as a foreign key to location table.
//                " FOREIGN KEY (" + FavouritesEntry.COLUMN_IMAGES_KEY + ") REFERENCES " +
//                ImagesEntry.TABLE_NAME + " (" + ImagesEntry._ID + "), " +
                // Ensure there is only one movie title
                " UNIQUE (" + FavouritesEntry.COLUMN_TITLE + ") ON CONFLICT REPLACE);";

//        sqLiteDatabase.execSQL(SQL_CREATE_IMAGES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ImagesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavouritesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
