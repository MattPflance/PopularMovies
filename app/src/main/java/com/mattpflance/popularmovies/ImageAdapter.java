package com.mattpflance.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by MattPflance on 2016-01-20.
 */
public class ImageAdapter extends BaseAdapter {

    private final String LOG_TAG = ImageAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<String> posterLinks = new ArrayList<>();

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        if (posterLinks != null) {
            return posterLinks.size();
        } else {
            return 0;
        }
    }

    public String getItem(int position) {
        return posterLinks.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public void add(String link) {
        posterLinks.add("http://image.tmdb.org/t/p/w500" + link);
        Log.v(LOG_TAG, "Length of posterLinks: " + posterLinks.size());
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public ImageView getView(int position, View convertView, ViewGroup parent) {
        ImageView view = (ImageView) convertView;
        if (view == null) {
            view = new ImageView(mContext);
        }
        String url = getItem(position);

        Picasso
                .with(mContext)
                .load(url)
                .into(view);
        return view;
    }
}
