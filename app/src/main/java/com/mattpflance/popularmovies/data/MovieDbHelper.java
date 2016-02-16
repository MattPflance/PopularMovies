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
    private static final int DATABASE_VERSION = 4;

    static final String DATABASE_NAME = "popularmovies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create table to hold a user's favourite movie

        final String SQL_CREATE_FAVOURITES_TABLE = "CREATE TABLE " + FavouritesEntry.TABLE_NAME + " (" +
                FavouritesEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_RELEASE + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_RATING + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_VOTES + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_POSTER + " BLOB, " +
                FavouritesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_VIDEOS + " TEXT, " +
                FavouritesEntry.COLUMN_REVIEW_AUTHOR + " TEXT, " +
                FavouritesEntry.COLUMN_REVIEW_CONTENT + " TEXT, " +
                // Ensure there is only one movie title
                " UNIQUE (" + FavouritesEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Just drop data and re-sync
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavouritesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
