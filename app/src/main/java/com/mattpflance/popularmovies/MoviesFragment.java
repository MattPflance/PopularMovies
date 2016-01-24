package com.mattpflance.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

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

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {

    private ImageAdapter mMoviesAdapter;
    private String sortingStr = "popularity.desc";

    public MoviesFragment() {
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // The adapter that takes data and populated the GridView attached to it
        mMoviesAdapter = new ImageAdapter(rootView.getContext(), new ArrayList<String>());

        // Now find the GridView we want to bind our adapter to
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movies);

        // Set the adapter to the GridView
        gridView.setAdapter(mMoviesAdapter);
//        gridList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                TextView forecastView = (TextView) view;
//                CharSequence forecastData = forecastView.getText();
//                Intent intent = new Intent(getActivity(), DetailActivity.class);
//                intent.putExtra(Intent.EXTRA_TEXT, forecastData);
//                startActivity(intent);
//            }
//        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMovies();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.movies_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort_by_popular) {
            sortingStr = "popularity.desc";
            loadMovies();
            return true;
        } else if (id == R.id.action_sort_by_high_rating) {
            // Include movies with at least 50 ratings for filtering
            sortingStr = "vote_average.desc";
            loadMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadMovies() {
        new FetchMoviesTask().execute();
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {
                // Construct the URL for the theMovieDB API

                // https://api.themoviedb.org/3/movie/550?api_key=1f223fc89d191807769dde2869ad3910
                final String API_KEY = "1f223fc89d191807769dde2869ad3910";
                final String FORECAST_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String KEY_PARAM = "api_key";
                final String COUNT_PARAM = "vote_count.gte";
                final String SORT_PARAM = "sort_by";

                Uri.Builder preBuiltUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, sortingStr);

                // Filter out "High Rated" movies that have less than 50 votes
                if (sortingStr.equals("vote_average.desc")) {
                    preBuiltUri.appendQueryParameter(COUNT_PARAM, "50");
                }

                Uri builtUri = preBuiltUri.appendQueryParameter(KEY_PARAM, API_KEY).build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "REQUEST FROM THIS URL" + url.toString());

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

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();


            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "ERROR: " + e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] poster_links) {
            // Clears the adapter to set new information
            super.onPostExecute(poster_links);
            if (poster_links != null) {
                mMoviesAdapter.clear();
                for (String link : poster_links) {
                    mMoviesAdapter.add(link);
                }
                mMoviesAdapter.notifyDataSetChanged();
            }
        }

        private String[] getMovieDataFromJson(String jsonStr) throws JSONException {

            final String MDB_RESULTS = "results";
//            final String MDB_TITLE = "original_title";
//            final String MDB_OVERVIEW = "overview";
//            final String MDB_RELEASE = "release_date";
            final String MDB_POSTER = "poster_path";

            JSONObject moviesJson = new JSONObject(jsonStr);

            JSONArray moviesArray = moviesJson.getJSONArray(MDB_RESULTS);

            int num_movies = moviesArray.length();

            String[] linksStr = new String[num_movies];

            for (int i=0; i<num_movies; i++) {
                JSONObject movie = moviesArray.getJSONObject(i);
//                String movie_title = movie.getJSONObject(MDB_TITLE).getString(MDB_TITLE);
//                String movie_overview = movie.getJSONObject(MDB_OVERVIEW).getString(MDB_OVERVIEW);
//                String movie_release_date = movie.getJSONObject(MDB_RELEASE).getString(MDB_RELEASE);
                String movie_poster = movie.getString(MDB_POSTER);
                linksStr[i] = movie_poster;
            }

            return linksStr;
        }
    }
}
