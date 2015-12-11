package com.example.sanch.myapplication;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sanch.myapplication.R;

import com.squareup.picasso.Picasso;
import java.net.HttpURLConnection;
import java.net.URL;

public class MovieAdapter extends ArrayAdapter {

    private final Context mContext;
    private ArrayList<MovieItem> mMovieData = new ArrayList<>(); ;


    public MovieAdapter(Context mContext, ArrayList<MovieItem> movieData) {
      super(mContext,0,movieData);
        this.mContext = mContext;
        this.mMovieData = movieData;
    }

public void setMovieData( MovieItem[] movieData){


    if(movieData != null){
        for (MovieItem movie : movieData) {
            add(movie);
        }
    }
}

    public void add(MovieItem object) {
        mMovieData.add(object);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (mMovieData.size());
    }

    @Override
    public MovieItem getItem(int position) {
        return (mMovieData.get(position));
    }


    static class ViewHolder{
                ImageView imageView;
    }


    @Override
    public View getView(int position, View rootView, ViewGroup parent) {

        View row = rootView;
        ViewHolder holder;

        if (row == null) {

            //inflater = ((Activity) mContext).getLayoutInflater();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.moviedb_image, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) row.findViewById(R.id.imageView_moviedb);
            row.setTag(holder);

        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

       // Using Picasso to Fetch Movie Posters and Load them into View
        URL url = null;
        final MovieItem movie = mMovieData.get(position);
        final String IMG_URL = " http://image.tmdb.org/t/p/w185" +movie.getPosterPath();
          try {

            url = new URL(IMG_URL);
            Log.v("Poster Path ...", IMG_URL);

            Picasso.with(mContext).load(String.valueOf(url)).error(R.drawable.ic_img_not_found).into(holder.imageView);


          } catch (MalformedURLException e) {

                e.printStackTrace();
          }
          return row;
        }
    }