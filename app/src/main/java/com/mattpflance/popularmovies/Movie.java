package com.mattpflance.popularmovies;

import android.media.Image;

import java.util.ArrayList;

/**
 * A class that holds a Movie's content
 */
public class Movie {

    private String mTitle, mOverview, mReleaseDate, mRating, mVotes, mId, mPosterLink;
//    private Image mPoster;
    private ArrayList<String> mTrailers, mReviews;

    public Movie (String title,
                  String overview,
                  String releaseDate,
                  String rating,
                  String votes,
                  String id,
                  String posterLink) {

        mTitle = title;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mRating = rating;
        mVotes = votes;
        mId = id;
        mPosterLink = posterLink;
//        mPoster = poster;
        mTrailers = null;
        mReviews = null;

    }

    public String getTitle() { return mTitle; }
    public String getOverview() { return mOverview; }
    public String getReleaseDate() { return mReleaseDate; }
    public String getRating() { return mRating; }
    public String getVotes() { return mVotes; }
    public String getId() { return mId; }
    public String getPosterLink() { return mPosterLink; }
//    public Image getPoster() {
//        return mPoster;
//    }
    public ArrayList<String> getTrailers() { return mTrailers; }
    public ArrayList<String> getReviews() { return mReviews; }

    public void setTrailers(ArrayList<String> trailers) { mTrailers = trailers; }
    public void setReviews(ArrayList<String> reviews) { mReviews = reviews; }

}
