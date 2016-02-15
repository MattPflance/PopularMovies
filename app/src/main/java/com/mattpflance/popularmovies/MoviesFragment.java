package com.mattpflance.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {

    private final String LOG_TAG = MoviesFragment.class.getSimpleName();
    private final String DEFAULT_SORT = "popularity.desc";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageAdapter mMoviesAdapter;

    private String mSortingStr;

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

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        mSortingStr = sharedPref.getString(getString(R.string.sort_key), DEFAULT_SORT);

        // Now find the RecyclerView we want to bind our adapter to
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.gridview_movies);

        // Set the LayoutManager to the RecyclerView
        int orientation = getResources().getConfiguration().orientation;
        int numColumns;
        if (orientation == 1) {
            numColumns = 2;
        } else {
            numColumns = 3;
        }

        mLayoutManager = new GridLayoutManager(getContext(), numColumns);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // The adapter that takes data and populated the GridView attached to it
        mMoviesAdapter = new ImageAdapter(rootView.getContext(), mRecyclerView, new ArrayList<Movie>());

        // Set the adapter to the RecyclerView
        mRecyclerView.setAdapter(mMoviesAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
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

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort_by_popular) {
            mSortingStr = "popularity.desc";
            editor.putString(getString(R.string.sort_key), mSortingStr);
            editor.commit();
            loadMovies();
            return true;
        } else if (id == R.id.action_sort_by_high_rating) {
            // Include movies with at least 50 ratings for filtering
            mSortingStr = "vote_average.desc";
            editor.putString(getString(R.string.sort_key), mSortingStr);
            editor.commit();
            loadMovies();
            return true;
        } else if (id == R.id.action_favourites) {
            // Include movies with at least 50 ratings for filtering
            mSortingStr = "favourites";
            editor.putString(getString(R.string.sort_key), mSortingStr);
            editor.commit();
            loadMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadMovies() {
        new FetchMoviesTask(mSortingStr, mMoviesAdapter).execute();
    }
}
