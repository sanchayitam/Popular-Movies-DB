package com.example.sanch.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MovieFragment extends Fragment {
    private MovieAdapter movieAdapter;
    private MovieItem[] mMovies;

    static final String MOVIES_KEYS = "movies";
    static final String SORT_KEY = "sorting key";
    private String POPULAR_MOVIES =  "popularity.desc";
    private String  HIGHLY_RATED = "vote_average.desc";
    private String mSort_key = POPULAR_MOVIES;


    public MovieFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();

       updateMovies(mSort_key);
    }
 // public interface Callback{ void onItemSelected(MovieItem movie);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

       final GridView mGridView = (GridView) rootView.findViewById(R.id.gridView);

        movieAdapter = new MovieAdapter(getActivity(), new ArrayList<MovieItem>());
        mGridView.setAdapter(movieAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                 // start the details screen
                 final  MovieItem movie = movieAdapter.getItem(position);

                 Log.v("what have I clicked on",movie.getTitle());
                 final  Intent intent = new Intent(getActivity(),DetailActivity.class);
                 intent.putExtra(MovieItem.EXTRA_MOVIES,movie);
                 startActivity(intent);

             }
         });

            if(savedInstanceState != null ) {
                if (savedInstanceState.containsKey(SORT_KEY)) {
                    mSort_key = savedInstanceState.getString(SORT_KEY);

                }
                if (savedInstanceState.containsKey(MOVIES_KEYS)){

                    mMovies =  (MovieItem[]) savedInstanceState.getParcelableArray(MOVIES_KEYS);

                    if(mMovies != null) {
                        movieAdapter.clear();
                        movieAdapter.notifyDataSetChanged();
                        movieAdapter.setMovieData(mMovies);

                    }

                }
                else{
                    updateMovies(mSort_key);
                    }
                } else {
                 updateMovies(mSort_key);
            }

        return rootView;
    }

    private void updateMovies(String sort_by)
    {

        FetchMovieDB MovieTask = new FetchMovieDB();
        MovieTask.execute(sort_by);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if(!mSort_key.contentEquals(POPULAR_MOVIES)){

            savedInstanceState.putString(SORT_KEY, mSort_key);
        }
        if(mMovies != null) {

            savedInstanceState.putParcelableArray(MOVIES_KEYS, mMovies);
        }

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
  //  super.onRestoreInstanceState(savedInstanceState);
        mSort_key = savedInstanceState.getString(SORT_KEY);
        mMovies =  (MovieItem[]) savedInstanceState.getParcelableArray(MOVIES_KEYS);

    }

   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear();
        inflater.inflate(R.menu.menu_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       int id = item.getItemId();

        switch (id) {
            case R.id.action_sort_by_highest:  {

                movieAdapter.clear();
                movieAdapter.notifyDataSetChanged();
                mSort_key = HIGHLY_RATED;
                updateMovies(mSort_key);
                 return true;
            }
            case R.id.action_sort_by_popular: {

                movieAdapter.clear();
                movieAdapter.notifyDataSetChanged();
                mSort_key = POPULAR_MOVIES;
                updateMovies(mSort_key);
                return  true;
            }
            case R.id.action_settings:

                return true;
            default:

                return super.onOptionsItemSelected(item);
        }

    }



    public class FetchMovieDB extends AsyncTask<String, Void, MovieItem[]> {
        final String LOG_TAG = FetchMovieDB.class.getSimpleName();

        @Override
        protected MovieItem[] doInBackground(String... params) {
            try {

                Log.v("Sort by key ..",params[0]);
                final String Movie_url = "http://api.themoviedb.org/3/discover/movie?";

                Uri.Builder builder = new Uri.Builder();
                builder.scheme("https").authority("api.themoviedb.org").appendPath("3")
                        .appendPath("discover").appendPath("movie")
                        .appendQueryParameter("sort_by", params[0])
                        .appendQueryParameter("api_key",getString(R.string.themoviesdb_api_key));


                URL url = new URL(builder.toString());
                Log.v("URL ...." ,builder.toString());
               // URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=57c8bf138eab4c78e72bc17cb9ab65e5");
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();

                // Read the input stream into a String
               String  inputStream = response.body().string();

                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                return parseJsonStr(inputStream);
            } catch (
                    IOException e
                    )
            {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            }
        }


    @Override
       protected void onPostExecute(MovieItem[] movies) {
        super.onPostExecute(movies);
         mMovies = movies;

        if (movies != null) {
            mMovies = movies;
            movieAdapter.setMovieData(mMovies);

        }
    }

        private MovieItem[] parseJsonStr(String movieJsonStr) {
            try {

                //Attributes of Json String
                final String M_PATH = "poster_path";
                final String M_TITLE = "original_title";
                final String M_ID = "id";
                final String M_OVERVIEW = "overview";
                final String M_USER_RATING = "vote_average";
                final String M_RELEASE_DATE = "release_date";

                //Convert Json String to Json Object
                JSONObject movieJsonObj = new JSONObject(movieJsonStr);

                //Get the Json Array
                JSONArray movieJsonArray = movieJsonObj.optJSONArray("results");

                MovieItem[] movieInfo = new MovieItem[movieJsonArray.length()];


                for (int i = 0; i < movieJsonArray.length(); i++) {
                    JSONObject post = movieJsonArray.optJSONObject(i);

                    movieInfo[i] = new MovieItem( post.getString(M_PATH),
                            post.getString(M_TITLE),
                            post.getString(M_OVERVIEW),
                            post.getString(M_USER_RATING),
                            post.getString(M_ID),
                            post.getString(M_RELEASE_DATE)
                    );

                    movieInfo[i].setPosterPath( post.getString(M_PATH));
                }

             return movieInfo;
            }   catch (JSONException e) {
                e.printStackTrace();
              }
               return null;
        }
    }

}






