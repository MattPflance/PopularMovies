package com.mattpflance.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by MattPflance on 2016-01-20.
 */
public class ImageAdapter extends BaseAdapter {

    private final String LOG_TAG = ImageAdapter.class.getSimpleName();

    private Context mContext = null;
    private List<String> posterLinks = null;

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
        return 0;
    }

    public void add(String link) {
        Log.v(LOG_TAG, "Link to image: " + link);
        if (link == "null") {
            Log.v(LOG_TAG, "ALAKSJDLKAJSDLKAJSLKFDJALKSDNLKAJSDLKAJSDLKJASLKDJLKASDJLKAJSDLKJASDLKJALSKDJLAKSFJLASFJLJPIQWEPIQUPRINL:ANLKCALKJSFLKJAPIJQ)PWHJR");
            posterLinks.add("http://www.aurangabadcity.com/img/client_images/image_not_available.jpg");
        } else {
            posterLinks.add("http://image.tmdb.org/t/p/w185" + link);
        }
    }

    public void clear() {
        posterLinks.clear();
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView posterView;
        if (convertView == null) {
            Log.v(LOG_TAG, "Make a new view!");
            posterView = new ImageView(mContext);
            posterView.setLayoutParams(new GridView.LayoutParams(parent.getWidth()/2, parent.getHeight()/2));
            posterView.setPadding(2, 2, 2, 2);
        } else {
            posterView = (ImageView) convertView;
        }
        String url = getItem(position);

        Log.v(LOG_TAG, "Position " + position + ": URL IS " + url);

        Picasso.with(mContext).load(url).into(posterView);
        return posterView;
    }
}
