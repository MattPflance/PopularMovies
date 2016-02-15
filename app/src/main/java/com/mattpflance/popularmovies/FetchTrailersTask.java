package com.mattpflance.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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

/**
 * An AsyncTask that grabs trailers for a particular movie
 */

public class FetchTrailersTask extends AsyncTask<String, Void, List<String>> {

    private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();
    private final String KEY_PARAM = "api_key";

    private Movie mMovie;
    private ImageAdapter mImageAdapter;

    public FetchTrailersTask(Movie movie, ImageAdapter imageAdapter) {
        mMovie = movie;
        mImageAdapter = imageAdapter;
    }

    @Override
    protected List<String> doInBackground(String... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String trailersStr = null;

        try {
            /**
             * Trailers
             */
            final String TRAILERS_BASE_URL = "http://api.themoviedb.org/3/discover/movie/" + mMovie.getId() + "/videos?";

            // Build the Uri to fetch trailers
            Uri uri = Uri.parse(TRAILERS_BASE_URL)
                    .buildUpon()
                    .appendQueryParameter(KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            // Create the URL
            URL url = new URL(uri.toString());

            // Create the request to theMovieDB, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
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
            if (buffer.length() == 0) return null;

            trailersStr = buffer.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, don't parse
            return null;
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
            if (reader != null) {
                try { reader.close(); }
                catch (final IOException e) { Log.e(LOG_TAG, "Error closing stream", e); }
            }
        }

        // Finally get data from the JSON string and
        // Additional movie data (Videos and Trailers)
        try {
            return getMovieDataFromJson(trailersStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "ERROR: " + e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<String> trailers) {
        // Clears the adapter to set new information
        super.onPostExecute(trailers);
        if (trailers != null) {
            /**
             * TODO ?
             */
            mMovie.setTrailers(trailers);
            mImageAdapter.notifyDataSetChanged();
        }
    }

    private List<String> getMovieDataFromJson(String trailersStr) throws JSONException {

        final String MDB_RESULTS = "results";
        final String MDB_NAME = "name";
        final String MDB_YOUTUBE_KEY = "key";

        JSONObject trailersJson = new JSONObject(trailersStr);
        JSONArray trailersArray = trailersJson.getJSONArray(MDB_RESULTS);

        int num_trailers = trailersArray.length();

        List<String> trailers = new ArrayList<>();

        for (int i = 0; i < num_trailers; i++) {
            // Grab trailer object
            JSONObject trailerInfo = trailersArray.getJSONObject(i);

            // Get author and content of trailer
            String trailerName = trailerInfo.getString(MDB_NAME);
            String trailerKey = trailerInfo.getString(MDB_YOUTUBE_KEY);

            // Add trailer author and content
            trailers.add(trailerName);
            trailers.add(trailerKey);

        }

        return trailers;
    }

}