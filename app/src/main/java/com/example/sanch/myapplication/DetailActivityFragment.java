package com.example.sanch.myapplication;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
   private  MovieItem mMovie;
    private TextView mTitle;
    private TextView mOverview;
    private TextView mDate;
    private TextView mVoteAverage;
    private ImageView mPosterImage;

    public DetailActivityFragment() {
            setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail,container,false);

        final Intent intent = getActivity().getIntent();
        if(intent != null && intent.hasExtra(MovieItem.EXTRA_MOVIES)){
             mMovie = intent.getParcelableExtra(MovieItem.EXTRA_MOVIES);
             mTitle =  (TextView) rootView.findViewById(R.id.detail_movie_title);
             mOverview = ((TextView) rootView.findViewById(R.id.detail_movie_plot));
             mVoteAverage = ((TextView) rootView.findViewById(R.id.detail_movie_user_rating));
             mDate = ((TextView) rootView.findViewById(R.id.detail_movie_release_date));

            mPosterImage = ((ImageView) rootView.findViewById(R.id.detail_poster_image_view));

            if(mMovie != null) {

                mTitle.setText(mMovie.getTitle());

                final String IMG_URL = " http://image.tmdb.org/t/p/w185" + mMovie.getPosterPath();
                URL url = null;
                try {

                    url = new URL(IMG_URL);
                    Log.v("Poster Path ...", IMG_URL);

                    Picasso.with(getActivity()).load(String.valueOf(url)).error(R.drawable.ic_img_not_found).into(mPosterImage);
                   } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                mDate.setText(mMovie.getRelease_date());
                mVoteAverage.setText(mMovie.getUser_rating()+ "/10");
                mOverview.setText(mMovie.getOverview());

            }
          }
          return rootView;
        }
    }
