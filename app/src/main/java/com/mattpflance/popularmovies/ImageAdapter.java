package com.mattpflance.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private final String LOG_TAG = ImageAdapter.class.getSimpleName();

    private Context mContext = null;
    private List<String> titles = new ArrayList<>();
    private List<String> posterLinks = null;
    private List<String> overviews = new ArrayList<>();
    private List<String> ratings = new ArrayList<>();
    private List<String> numVotes = new ArrayList<>();
    private List<String> releaseDates = new ArrayList<>();

    public ImageAdapter(Context c, List<String> links) {
        mContext = c;
        posterLinks = links;
    }

    public int getCount() {
        return (posterLinks != null) ? posterLinks.size() : 0;
    }

    public String getItem(int position) {
        return (posterLinks != null) ? posterLinks.get(position) : null;
    }

    public long getItemId(int position) {
        return position;
    }

    public void add(String[] movieData) {
        titles.add(movieData[0]);
        if (movieData[1] == "null") {
            posterLinks.add("http://www.aurangabadcity.com/img/client_images/image_not_available.jpg");
        } else {
            posterLinks.add("http://image.tmdb.org/t/p/w185" + movieData[1]);
        }
        overviews.add(movieData[2]);
        ratings.add(movieData[3]);
        numVotes.add(movieData[4]);
        releaseDates.add(movieData[5]);
    }

    public String getTitle(int position) { return titles.get(position); }
    public String getPoster(int position) { return posterLinks.get(position); }
    public String getOverview(int position) { return overviews.get(position); }
    public String getRatings(int position) { return ratings.get(position); }
    public String getNumVotes(int position) { return numVotes.get(position); }
    public String getDates(int position) { return releaseDates.get(position); }

    public void clear() {
        if (titles != null) titles.clear();
        if (posterLinks != null) posterLinks.clear();
        if (overviews != null) overviews.clear();
        if (ratings != null) ratings.clear();
        if (numVotes != null) numVotes.clear();
        if (releaseDates != null) releaseDates.clear();
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView posterView;
        if (convertView == null) {
            posterView = new ImageView(mContext);
            posterView.setLayoutParams(new GridView.LayoutParams(parent.getWidth()/2, parent.getHeight()/2));
            posterView.setPadding(2, 2, 2, 2);
        } else {
            posterView = (ImageView) convertView;
        }
        String url = getItem(position);

        Picasso.with(mContext).load(url).into(posterView);
        return posterView;
    }
}
