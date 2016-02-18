package com.mattpflance.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Defines table and column names for the movies database.
 */
public class MovieContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.mattpflance.popularmovies.app";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    // The path for our database
    public static final String PATH_FAVOURITES = "favourites";
    public static final String PATH_MOVIE = "movie";

    /* Inner class that defines the table contents of the weather table */
    public static final class FavouritesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITES).build();
        public static final Uri CONTENT_ITEM_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVOURITES)
                .appendPath(PATH_MOVIE)
                .build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITES;

        public static final String TABLE_NAME = "favourites";

        // Column with Movie ID
        public static final String COLUMN_MOVIE_ID = "id";

        // Column with movie title
        public static final String COLUMN_TITLE = "original_title";

        // Column containing the release date
        public static final String COLUMN_RELEASE = "release_date";

        // Column with the movie's rating out of 10
        public static final String COLUMN_RATING = "vote_average";

        // Column with the amount votes a movie has
        public static final String COLUMN_VOTES = "vote_count";

        // Column that holds the movie poster image stored as a BLOB byte[]
        public static final String COLUMN_POSTER = "poster";

        // Column with movie overview
        public static final String COLUMN_OVERVIEW = "overview";

        // Column that holds the YouTube key for videos
        public static final String COLUMN_VIDEOS = "videos";

        // Column that holds reviewer's name
        public static final String COLUMN_REVIEW_AUTHOR = "author";

        // Column that holds review's content
        public static final String COLUMN_REVIEW_CONTENT = "review";

    }
}
