package com.mattpflance.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.TextView;

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

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private static ImageAdapter mMoviesAdapter;

    public MoviesFragment() {
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Now find the RecyclerView we want to bind our adapter to
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_movies);

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

    public static ImageAdapter getMoviesAdapter() { return mMoviesAdapter; }

}
