package com.mattpflance.popularmovies;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mattpflance.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    private final String LOG_TAG = DetailFragment.class.getSimpleName();

    private Context mContext;
    private ShareActionProvider mShareActionProvider;

    /**
     * Alternatively, we could implement Parcelable for Movie
     * and pass it through the intent. Then we only have 1
     * private variable that is a Movie.
     */

    private String mId, mTitle, mReleaseDate, mRating, mVotes,  mPosterLink, mOverview;
    private Bitmap mPosterBitmap;
    private ArrayList<String> mTrailers, mReviewAuthors, mReviews;

    public DetailFragment() { setHasOptionsMenu(true); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        final Bundle bundle = getArguments();

        if (bundle != null) {

            // Get all the movie values from intent

            view.setVisibility(View.VISIBLE);

            mId = bundle.getString("ID");
            mTitle = bundle.getString("TITLE");
            mReleaseDate = bundle.getString("DATE");
            mRating = bundle.getString("RATING");
            mVotes = bundle.getString("VOTES");
            mPosterLink = bundle.getString("LINK");
            mPosterBitmap = bundle.getParcelable("POSTER");
            mOverview = bundle.getString("OVERVIEW");
            mTrailers = bundle.getStringArrayList("VIDEOS");
            mReviewAuthors = bundle.getStringArrayList("AUTHORS");
            mReviews = bundle.getStringArrayList("REVIEWS");
        } else {

            // Implement call back to fix this problem

            view.setVisibility(View.GONE);

            return view;

        }

        // Set all the Views with the values
        ((TextView) view.findViewById(R.id.movie_title)).setText(mTitle);
        ((TextView) view.findViewById(R.id.release_date))
                .setText(String.format(getString(R.string.format_release_date), mReleaseDate));
        ((TextView) view.findViewById(R.id.rating))
                .setText(String.format(mContext.getString(R.string.format_ratings), mRating));
        ((TextView) view.findViewById(R.id.votes))
                .setText(String.format(mContext.getString(R.string.format_votes), mVotes));
        ((TextView) view.findViewById(R.id.overview)).setText(mOverview);
        // Declare final so we can use in the onClick listener
        final ImageView iv = (ImageView) view.findViewById(R.id.movie_thumbnail);
        if (mPosterBitmap != null) {
            // Loading from Cursor
            iv.setImageBitmap(mPosterBitmap);

        } else if (mPosterLink != null) {
            // Loading with API
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185" + mPosterLink).into(iv);
        } else {
            // No image
            Picasso.with(getActivity()).load(R.drawable.image_not_available).into(iv);
        }

        if (mPosterBitmap != null) {

            // Loading from cursor and we show the Remove From Favourites button

            ImageButton ib = (ImageButton) view.findViewById(R.id.favourite_button);
            ib.setBackgroundResource(R.drawable.unfavourite);
            ib.setContentDescription(getString(R.string.unfavourite_button));
            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getContext().getContentResolver().delete(MovieContract.FavouritesEntry.CONTENT_ITEM_URI,
                            MovieContract.FavouritesEntry.COLUMN_MOVIE_ID + " = " + mId,
                            null);

                    // Show Toast to let user know that the movie was deleted from db
                    CharSequence toastMsg = String.format(getString(R.string.toast_delete), mTitle);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getContext(), toastMsg, duration);
                    toast.show();
                }
            });

        } else {

            // We are loading from API and we show the Favourites button

            ImageButton ib = (ImageButton) view.findViewById(R.id.favourite_button);
            ib.setBackgroundResource(R.drawable.favourite_star);
            ib.setContentDescription(getString(R.string.favourite_button));
            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ContentValues movieValues = new ContentValues();

                    movieValues.put(MovieContract.FavouritesEntry.COLUMN_MOVIE_ID, mId);
                    movieValues.put(MovieContract.FavouritesEntry.COLUMN_TITLE, mTitle);
                    movieValues.put(MovieContract.FavouritesEntry.COLUMN_RELEASE, mReleaseDate);
                    movieValues.put(MovieContract.FavouritesEntry.COLUMN_RATING, mRating);
                    movieValues.put(MovieContract.FavouritesEntry.COLUMN_VOTES, mVotes);
                    // Convert image from poster to bitmap
                    Bitmap bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
                    // Place in DB using a BLOB (byte[])
                    movieValues.put(MovieContract.FavouritesEntry.COLUMN_POSTER, Utility.getBytes(bitmap));
                    movieValues.put(MovieContract.FavouritesEntry.COLUMN_OVERVIEW, mOverview);
                    // These next ArrayList<String>s are converted to Strings
                    movieValues.put(MovieContract.FavouritesEntry.COLUMN_VIDEOS, Utility.listToString(mTrailers));
                    movieValues.put(MovieContract.FavouritesEntry.COLUMN_REVIEW_AUTHOR, Utility.listToString(mReviewAuthors));
                    movieValues.put(MovieContract.FavouritesEntry.COLUMN_REVIEW_CONTENT, Utility.listToString(mReviews));

                    // Now add this to the data base
                    getContext().getContentResolver().insert(MovieContract.FavouritesEntry.CONTENT_ITEM_URI, movieValues);

                    // Show Toast to let user know that the movie was stored in the DB
                    CharSequence toastMsg = String.format(getString(R.string.toast_add), mTitle);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getContext(), toastMsg, duration);
                    toast.show();
                }
            });
        }
        /**
         * Now we need to dynamically add Trailers and Reviews
         */

        // Create a linear to store all this data
        LinearLayout dynamicLL = new LinearLayout(mContext);
        dynamicLL.setOrientation(LinearLayout.VERTICAL);
        dynamicLL.setId(R.id.dynamic_details);

        // Set the layout params for the LinearLayout
        RelativeLayout.LayoutParams dynamicLLParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dynamicLLParams.addRule(RelativeLayout.BELOW, R.id.overview);
        dynamicLLParams.setMargins(Utility.dpToPx(mContext, 20), 0, 0, 0);

        // Title for Trailers
        TextView trailerTitle = new TextView(mContext);
        trailerTitle.setTextSize(Utility.dpToPx(mContext, 10));
        trailerTitle.setText(getString(R.string.trailers));
        dynamicLL.addView(trailerTitle);

        if (mTrailers != null) {

            // Get the length of trailers
            int length = mTrailers.size();

            // Create layout for Trailers
            for (int i=0; i<length; i++) {

                // Create the new LinearLayout

                int llPad = Utility.dpToPx(mContext, 15);

                LinearLayout ll = new LinearLayout(mContext);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.setGravity(Gravity.CENTER_VERTICAL);
                ll.setPadding(llPad, llPad, llPad, llPad);
                ll.setId(R.id.trailers_layout);

                // Text View for Trailer
                TextView tv = new TextView(mContext);
                tv.setTextSize(Utility.dpToPx(mContext, 8));
                tv.setText(String.format(mContext.getString(R.string.format_trailer), i+1));
                ll.addView(tv);

                // Set Image for playing trailer and set text to
                // the YouTube key for the trailer
                ImageButton imageButton = new ImageButton(mContext);
                final int trailerPosition = i;
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Use key to launch YouTube intent
                        String key = mTrailers.get(trailerPosition);
                        startActivity(new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://www.youtube.com/watch?v=" + mTrailers.get(trailerPosition))
                        ));
                    }
                });
                LinearLayout.LayoutParams imageButtonParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
                );
                imageButton.setBackgroundResource(R.drawable.play_image);
                ll.addView(imageButton, imageButtonParams);

                // Finally, add the Linear Layout to the ViewGroup with Params
                dynamicLL.addView(ll);
            }

        } else {
            // There are no trailers

            TextView tv = new TextView(mContext);
            tv.setTextSize(Utility.dpToPx(mContext, 6));
            tv.setText(getString(R.string.no_trailers));
            dynamicLL.addView(tv);
        }

        // Title for Reviews
        TextView reviewTitle = new TextView(mContext);
        reviewTitle.setTextSize(Utility.dpToPx(mContext, 10));
        reviewTitle.setText(getString(R.string.reviews));
        dynamicLL.addView(reviewTitle);

        if (mReviews != null && mReviews.size() != 0) {

            // Get the length of reviews AND authors
            int length = mReviews.size();

            // Create layout for Reviews
            for (int i=0; i<length; i ++) {

                // Text View for Review Author
                TextView authorTV = new TextView(mContext);
                authorTV.setTextAppearance(mContext, R.style.boldText);
                authorTV.setTextSize(Utility.dpToPx(mContext, 6));
                authorTV.setText(String.format(mContext.getString(R.string.format_review_author), mReviewAuthors.get(i)));
                dynamicLL.addView(authorTV);

                // Text View for the review's content
                int pad = Utility.dpToPx(mContext, 5);

                TextView contentTV = new TextView(mContext);
                contentTV.setPadding(pad, pad, pad, pad);
                contentTV.setTextSize(Utility.dpToPx(mContext, 5));
                contentTV.setText(mReviews.get(i));
                dynamicLL.addView(contentTV);

            }

        } else {
            // There are no reviews
            TextView tv = new TextView(mContext);
            tv.setTextSize(Utility.dpToPx(mContext, 6));
            tv.setText(getString(R.string.no_reviews));
            dynamicLL.addView(tv);
        }

        // Grab the parent we need to attach the new LinearLayout to
        RelativeLayout parentView = (RelativeLayout) view.findViewById(R.id.detail_scroll_view_layout);

        // Finally, add the dynamic linear layout to the parent
        parentView.addView(dynamicLL, dynamicLLParams);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.details_menu, menu);

        // get share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        mShareActionProvider.setShareIntent(createShareForecastIntent());
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        String text = "https://www.youtube.com/watch?v=";
        if (mTrailers != null) text += mTrailers.get(0);
        else text += "dQw4w9WgXcQ"; // XDDDDDD
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        return shareIntent;
    }

}
