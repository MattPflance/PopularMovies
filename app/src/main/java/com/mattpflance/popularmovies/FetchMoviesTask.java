package com.mattpflance.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.mattpflance.popularmovies.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    private final String KEY_PARAM = "api_key";

    private Context mContext;
    private String mSortingStr;
    private ImageAdapter mMoviesAdapter;

    public FetchMoviesTask(Context context, String sort, ImageAdapter imageAdapter) {
        mContext = context;
        mSortingStr = sort;
        mMoviesAdapter = imageAdapter;
    }

    @Override
    protected List<Movie> doInBackground(String... params) {

        // First check to see if we want to load our favourites or not
        if (mSortingStr.equals("favourites")) {
            Log.v(LOG_TAG, "In");
            return loadFavourites();
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String detailsStr = null;

        try {
            final String DETAILS_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
            final String COUNT_PARAM = "vote_count.gte";
            final String SORT_PARAM = "sort_by";


            Uri.Builder preBuiltUri = Uri.parse(DETAILS_BASE_URL).buildUpon().appendQueryParameter(SORT_PARAM, mSortingStr);
            // Filter out "High Rated" movies that have less than 50 votes
            if (mSortingStr.equals("vote_average.desc")) {
                preBuiltUri.appendQueryParameter(COUNT_PARAM, "50");
            }
            Uri detailsURI = preBuiltUri.appendQueryParameter(KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY).build();
            URL detailsURL = new URL(detailsURI.toString());

            // Create the request to theMovieDB, and open the connection
            urlConnection = (HttpURLConnection) detailsURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            detailsStr = buffer.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, don't parse
            return null;
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
            if (reader != null) {
                try { reader.close(); } catch (final IOException e) { Log.e(LOG_TAG, "Error closing stream", e); }
            }
        }

        // Finally get data from the JSON string and
        // Additional movie data (Videos and Reviews)
        try {
            return getMovieDataFromJson(detailsStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "ERROR: " + e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        // Clears the adapter to set new information
        super.onPostExecute(movies);
        if (movies != null) {
            mMoviesAdapter.clear();
            for (Movie movie : movies) {
                // Don't need to make a request to API if favourites
                if (mSortingStr != "favourites") {
                    // COOOOOOKIIIEEEEEE
                    new FetchTrailersTask(movie).execute();
                    new FetchReviewsTask(movie).execute();
                }
                mMoviesAdapter.add(movie);
            }
            mMoviesAdapter.notifyDataSetChanged();
        }
    }

    private List<Movie> getMovieDataFromJson(String detailsStr) throws JSONException {
        final String MDB_RESULTS = "results";
        final String MDB_TITLE = "original_title";
        final String MDB_OVERVIEW = "overview";
        final String MDB_RELEASE = "release_date";
        final String MDB_RATING = "vote_average";
        final String MDB_VOTES = "vote_count";
        final String MDB_MOVIE_ID = "id";
        final String MDB_POSTER = "poster_path";

        JSONObject moviesJson = new JSONObject(detailsStr);
        JSONArray moviesArray = moviesJson.getJSONArray(MDB_RESULTS);

        int num_movies = moviesArray.length();

        List<Movie> movies = new ArrayList<>();

        for (int i = 0; i < num_movies; i++) {
            JSONObject movie = moviesArray.getJSONObject(i);

            movies.add(new Movie(
                    movie.getString(MDB_MOVIE_ID),
                    movie.getString(MDB_TITLE),
                    movie.getString(MDB_RELEASE),
                    movie.getString(MDB_RATING),
                    movie.getString(MDB_VOTES),
                    movie.getString(MDB_POSTER),
                    movie.getString(MDB_OVERVIEW)
            ));

        }

        return movies;

    }

    private List<Movie> loadFavourites() {
        // Get cursor of all movies
        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.FavouritesEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        // Return if cursor is empty
        if (cursor == null) return null;

        ArrayList<Movie> movies = new ArrayList<>();

        while (cursor.moveToNext()) {

            movies.add(new Movie(cursor));

        }

        cursor.close();

        return movies;
    }

    }