package com.mattpflance.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private final String LOG_TAG = ImageAdapter.class.getSimpleName();

    private Context mContext;
    private RecyclerView mRecyclerView;

    /**
     * List of Movies
     */
    private List<Movie> mDataSet;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView posterView;
        public View selectedView;

        public ViewHolder(View view) {
            super(view);
            posterView = (ImageView) view.findViewById(R.id.movie_poster_image);
            selectedView = view.findViewById(R.id.selector);
        }

    }

    public ImageAdapter(Context c, RecyclerView recyclerView, List<Movie> dataSet) {
        mContext = c;
        mRecyclerView = recyclerView;
        mDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Called when RecyclerView needs a new RecyclerView.ViewHolder
        // of the given type to represent an item.
        final View movieItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        movieItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Add data into a bundle
                Bundle bundle = new Bundle();
                int itemPosition = mRecyclerView.getChildLayoutPosition(view);
                Movie movie = mDataSet.get(itemPosition);

                bundle.putString("ID", movie.getId());
                bundle.putString("TITLE", movie.getTitle());
                bundle.putString("DATE", movie.getReleaseDate());
                bundle.putString("RATING", movie.getRating());
                bundle.putString("VOTES", movie.getVotes());
                bundle.putString("LINK", movie.getPosterLink());
                bundle.putParcelable("POSTER", movie.getPosterBitmap());
                bundle.putString("OVERVIEW", movie.getOverview());
                bundle.putStringArrayList("VIDEOS", movie.getTrailers());
                bundle.putStringArrayList("AUTHORS", movie.getReviewAuthors());
                bundle.putStringArrayList("REVIEWS", movie.getReviews());

                if (MainActivity.getTwoPane()) {

                    // Remove any outstanding selections
                    for (Movie m : mDataSet) {
                        m.setSelected(false);
                    }
                    // Update selection
                    movie.setSelected(true);

                    // Notify movies have been changed
                    notifyDataSetChanged();

                    // Replace the fragment and do not launch intent
                    DetailFragment fragment = new DetailFragment();
                    fragment.setArguments(bundle);

                    ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.detail_container, fragment, MainActivity.getDetailfragmentTag())
                            .commit();
                } else {

                    // Launch intent
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            }
        });
        return new ViewHolder(movieItemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        String posterLink = getMovie(position).getPosterLink();
        Bitmap poster = getMovie(position).getPosterBitmap();
        if (posterLink == null && poster == null) {
            // There is actually no image
            Picasso.with(mContext).load(R.drawable.image_not_available).into(viewHolder.posterView);
        } else if (poster == null) {
            // Grabbing the image from API
            Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185" + posterLink).into(viewHolder.posterView);
        } else {
            // Loading from favourites
            viewHolder.posterView.setImageBitmap(poster);
        }

        if (getMovie(position).isSelected()) {
            viewHolder.selectedView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.selectedView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() { return (mDataSet != null) ? mDataSet.size() : 0; }

    public long getItemId(int position) { return position; }

    public void add(Movie movie) { mDataSet.add(movie); }
    public void clear() { mDataSet.clear(); }

    public Movie getMovie(int position) { return mDataSet.get(position); }

    public List<Movie> getDataSet() { return mDataSet; }

}
