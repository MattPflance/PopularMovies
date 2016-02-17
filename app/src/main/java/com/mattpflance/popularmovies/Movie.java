package com.mattpflance.popularmovies;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;

import java.util.ArrayList;

/**
 * A class that holds a Movie's content
 */
public class Movie {

    private String mId, mTitle, mReleaseDate, mRating, mVotes,  mPosterLink, mOverview;
    private Bitmap mPosterBitmap;
    private ArrayList<String> mTrailers, mReviewAuthors, mReviews;
    private boolean mIsSelected; // This is for tablet views

    // The constructor that the API will use
    public Movie (String id,
                  String title,
                  String releaseDate,
                  String rating,
                  String votes,
                  String posterLink,
                  String overview) {
        mId = id;
        mTitle = title;
        mReleaseDate = releaseDate;
        mRating = rating;
        mVotes = votes;
        mPosterLink = posterLink;
        mPosterBitmap = null; // Not used with API
        mOverview = overview;
        mTrailers = null;
        // Need to initialize these next lists since we add content
        // to them and there is no assignment
        mReviewAuthors = new ArrayList<>();
        mReviews = new ArrayList<>();
        mIsSelected = false;
    }

    // The constructor used when retrieving from a cursor
    public Movie (Cursor cursor) {

        final int COL_MOVIE_ID = 0;
        final int COL_TITLE = 1;
        final int COL_RELEASE_DATE = 2;
        final int COL_RATING = 3;
        final int COL_VOTES = 4;
        final int COL_POSTER = 5;
        final int COL_OVERVIEW = 6;
        final int COL_VIDEOS = 7;
        final int COL_REVIEW_AUTHOR = 8;
        final int COL_REVIEW_CONTENT = 9;

        mId = cursor.getString(COL_MOVIE_ID);
        mTitle = cursor.getString(COL_TITLE);
        mReleaseDate = cursor.getString(COL_RELEASE_DATE);
        mRating = cursor.getString(COL_RATING);
        mVotes = cursor.getString(COL_VOTES);
        mPosterLink = null; // Not used with Cursor
        mPosterBitmap = Utility.getImage(cursor.getBlob(COL_POSTER));
        mOverview = cursor.getString(COL_OVERVIEW);
        mTrailers = Utility.stringToList(cursor.getString(COL_VIDEOS));
        mReviewAuthors = Utility.stringToList(cursor.getString(COL_REVIEW_AUTHOR));
        mReviews = Utility.stringToList(cursor.getString(COL_REVIEW_CONTENT));
        mIsSelected = false;
    }

    public String getTitle() { return mTitle; }
    public String getOverview() { return mOverview; }
    public String getReleaseDate() { return mReleaseDate; }
    public String getRating() { return mRating; }
    public String getVotes() { return mVotes; }
    public String getId() { return mId; }
    public String getPosterLink() { return mPosterLink; }
    public Bitmap getPosterBitmap() { return mPosterBitmap; }
    public ArrayList<String> getTrailers() { return mTrailers; }
    public ArrayList<String> getReviews() { return mReviews; }
    public ArrayList<String> getReviewAuthors() { return mReviewAuthors; }
    public boolean isSelected() { return mIsSelected; }

    public void setTrailers(ArrayList<String> trailers) { mTrailers = trailers; }
    public void addReviewAuthor(String author) { mReviewAuthors.add(author); }
    public void addReview(String review) { mReviews.add(review); }
    public void setSelected(boolean selection) { mIsSelected = selection; }

}
