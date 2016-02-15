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
 * An AsyncTask that grabs reviews for a particular movie
 */

public class FetchReviewsTask extends AsyncTask<String, Void, ArrayList<String>> {

    private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();
    private final String KEY_PARAM = "api_key";

    private Movie mMovie;
    private ImageAdapter mImageAdapter;

    public FetchReviewsTask(Movie movie, ImageAdapter imageAdapter) {
        mMovie = movie;
        mImageAdapter = imageAdapter;
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String reviewsStr = null;

        try {
            final String REVIEWS_BASE_URL = "http://api.themoviedb.org/3/movie/" + mMovie.getId() + "/reviews?";

            // Build the Uri to fetch reviews
            Uri uri = Uri.parse(REVIEWS_BASE_URL)
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

            reviewsStr = buffer.toString();

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
        // Additional movie data (Videos and Reviews)
        try {
            return getMovieDataFromJson(reviewsStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "ERROR: " + e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<String> reviews) {
        // Clears the adapter to set new information
        super.onPostExecute(reviews);
        if (reviews != null) {
            /**
             * TODO ?
             */
            mMovie.setReviews(reviews);
            mImageAdapter.notifyDataSetChanged();
        }
    }

    private ArrayList<String> getMovieDataFromJson(String reviewsStr) throws JSONException {

        final String MDB_RESULTS = "results";
        final String MDB_AUTHOR = "author";
        final String MDB_CONTENT = "content";

        JSONObject reviewsJson = new JSONObject(reviewsStr);
        JSONArray reviewsArray = reviewsJson.getJSONArray(MDB_RESULTS);

        int num_reviews = reviewsArray.length();

        if (num_reviews == 0) return null;

        ArrayList<String> reviews = new ArrayList<>();

        for (int i = 0; i < num_reviews; i++) {
            // Grab review object
            JSONObject reviewInfo = reviewsArray.getJSONObject(i);

            // Get author and content of review
            String reviewAuthor = reviewInfo.getString(MDB_AUTHOR);
            String reviewContent = reviewInfo.getString(MDB_CONTENT);

            // Add review author and content
            reviews.add(reviewAuthor);
            reviews.add(reviewContent);

        }

        return reviews;
    }

}
