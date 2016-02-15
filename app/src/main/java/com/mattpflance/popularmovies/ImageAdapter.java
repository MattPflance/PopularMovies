package com.mattpflance.popularmovies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
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

        public ViewHolder(View view) {
            super(view);
            posterView = (ImageView) view.findViewById(R.id.movie_poster_image);
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
        View movieItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        movieItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemPosition = mRecyclerView.getChildAdapterPosition(view);
                Movie movie = mDataSet.get(itemPosition);
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("TITLE", movie.getTitle());
                intent.putExtra("LINK", movie.getPosterLink());
                intent.putExtra("OVERVIEW", movie.getOverview());
                intent.putExtra("RATING", movie.getRating());
                intent.putExtra("VOTES", movie.getVotes());
                intent.putExtra("DATE", movie.getReleaseDate());
                intent.putStringArrayListExtra("TRAILERS", movie.getTrailers());
                intent.putStringArrayListExtra("REVIEWS", movie.getReviews());
                mContext.startActivity(intent);
            }
        });
        return new ViewHolder(movieItemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        String posterLink = getMovie(position).getPosterLink();
        if (posterLink == null) {
            Picasso.with(mContext).load(R.drawable.image_not_available).into(viewHolder.posterView);
        } else {
            Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185" + posterLink).into(viewHolder.posterView);
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
