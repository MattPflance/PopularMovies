package com.mattpflance.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

        return view;
    }



}
