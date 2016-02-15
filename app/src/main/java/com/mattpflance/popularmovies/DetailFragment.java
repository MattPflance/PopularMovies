package com.mattpflance.popularmovies;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    private Context mContext;

    private TextView mTitle;
    private ImageView mThumbnail;
    private TextView mReleaseDate;
    private TextView mRating;
    private ImageButton mFavouriteButton;
    private TextView mOverview;
    private ArrayList<String> mTrailers;
    private ArrayList<String> mReviews;

    public DetailFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();

        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        Bundle bundle = getArguments();

        mTitle = (TextView) view.findViewById(R.id.movie_title);
        mTitle.setText(bundle.getString("TITLE"));

        mThumbnail = (ImageView) view.findViewById(R.id.movie_thumbnail);
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185" + bundle.getString("LINK")).into(mThumbnail);

        mReleaseDate = (TextView) view.findViewById(R.id.release_date);
        mReleaseDate.setText(String.format(mContext.getString(R.string.format_release_date), bundle.getString("DATE")));

        mRating = (TextView) view.findViewById(R.id.rating);
        mRating.setText(String.format(mContext.getString(R.string.format_ratings), bundle.getString("RATING"),  bundle.getString("VOTES")));

        mFavouriteButton = (ImageButton) view.findViewById(R.id.favourite_button);
        mFavouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence toastMsg = mTitle.getText() + " added to Favourites";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getContext(), toastMsg, duration);
                toast.show();
            }
        });

        mOverview = (TextView) view.findViewById(R.id.overview);
        mOverview.setText(bundle.getString("OVERVIEW"));

        // Now we need to dynamically add Trailers and Reviews
        mTrailers = bundle.getStringArrayList("TRAILERS");
        if (mTrailers != null) {

            // Get the length of trailers
            int length = mTrailers.size();

            // Grab the parent we need to attach the new LinearLayout to
            RelativeLayout parentView = (RelativeLayout) view.findViewById(R.id.detail_scroll_view_layout);

            // Set the layout params for the LinearLayout
            RelativeLayout.LayoutParams llParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            llParams.addRule(RelativeLayout.BELOW, R.id.overview);

            llParams.setMargins(Utility.dpToPx(mContext, 20), 0, 0, 0);

            // Create the new LinearLayout
            LinearLayout ll = new LinearLayout(mContext);
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.setId(R.id.trailers_layout);

            // Create layout for Trailers
            for (int i=0; i<length; i++) {
                // Text View for Trailer
                TextView tv = new TextView(mContext);
                tv.setTextSize(Utility.dpToPx(mContext, 10));
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
                    }
                });
                LinearLayout.LayoutParams imageButtonParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
                );
                imageButton.setBackgroundResource(R.drawable.play_image);
                ll.addView(imageButton, imageButtonParams);
            }

            // Finally, add the Linear Layout to the ViewGroup with Params
            parentView.addView(ll, llParams);
        }

//        mReviews = bundle.getStringArrayList("REVIEWS");
//        if (mReviews != null) {
//
//            // Get the length of trailers
//            int length = mReviews.size();
//
//            // Grab the parent we need to attach the new LinearLayout to
//            RelativeLayout parentView = (RelativeLayout) view.findViewById(R.id.detail_scroll_view_layout);
//
//            // Set the layout params for the LinearLayout
//            RelativeLayout.LayoutParams llParams = new RelativeLayout.LayoutParams(
//                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            llParams.addRule(RelativeLayout.BELOW, R.id.overview);
//
//            llParams.setMargins(Utility.dpToPx(mContext, 20), 0, 0, 0);
//
//            // Create the new LinearLayout
//            LinearLayout ll = new LinearLayout(mContext);
//            ll.setOrientation(LinearLayout.VERTICAL);
//            ll.setId(R.id.reviews_layout);
//
//            // Create layout for Trailers
//            for (int i=0; i<length; i++) {
//                // Text View for Trailer
//                TextView tv = new TextView(mContext);
//                tv.setTextSize(Utility.dpToPx(mContext, 10));
//                tv.setText(String.format(mContext.getString(R.string.format_trailer), i+1));
//                ll.addView(tv);
//
//                // Set Image for playing trailer and set text to
//                // the YouTube key for the trailer
//                ImageButton imageButton = new ImageButton(mContext);
//                LinearLayout.LayoutParams imageButtonParams = new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
//                );
//                imageButton.setBackgroundResource(R.drawable.play_image);
//                ll.addView(imageButton, imageButtonParams);
//            }
//
//            // Finally, add the Linear Layout to the ViewGroup with Params
//            parentView.addView(ll, llParams);
//        }

        return view;
    }



}
