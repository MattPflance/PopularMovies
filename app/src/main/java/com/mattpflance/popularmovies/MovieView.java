package com.mattpflance.popularmovies;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MovieView extends ImageView {

    private String title, posterLink, overview, rating, releaseDate;

    public MovieView(Context context) {
        super(context);
    }

    public MovieView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MovieView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MovieView(Context context, String t, String p, String o, String rate, String release) {
        super(context);
        title = t; posterLink = p; overview = o; rating = rate; releaseDate = release;
    }

    public String getTitle() { return title; }
    public String getPosterLink() { return posterLink; }
    public String getOverview() { return overview; }
    public String getRating() { return rating; }
    public String getReleaseDate() { return releaseDate; }
}
