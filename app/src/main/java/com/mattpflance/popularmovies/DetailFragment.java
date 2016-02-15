package com.mattpflance.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        Bundle bundle = getArguments();

        TextView title = (TextView) view.findViewById(R.id.movie_title);
        title.setText(bundle.getString("TITLE"));

        ImageView thumbnail = (ImageView) view.findViewById(R.id.movie_thumbnail);
        String url = "http://image.tmdb.org/t/p/w185" + bundle.getString("LINK");
        Picasso.with(getActivity()).load(url).into(thumbnail);

        title = (TextView) view.findViewById(R.id.release_date);
        String dateStr = "Released " + bundle.getString("DATE");
        title.setText(dateStr);

        title = (TextView) view.findViewById(R.id.rating);
        String ratingsStr = bundle.getString("RATING") + "/10 from " + bundle.getString("VOTES") + " votes";
        title.setText(ratingsStr);

        title = (TextView) view.findViewById(R.id.overview);
        title.setText(bundle.getString("OVERVIEW"));



        return view;
    }



}
