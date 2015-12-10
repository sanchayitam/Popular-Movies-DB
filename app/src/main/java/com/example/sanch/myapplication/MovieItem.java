package com.example.sanch.myapplication;
import android.graphics.Movie;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sanch on 12/2/2015.
 */
public class MovieItem implements Parcelable {
    private String imgUrl;
    private String title;
    private String overview;
    private String user_rating;
    private String id;
    private String release_date;
    public static final String EXTRA_MOVIES = "extra movies";
    public MovieItem(String url ,String title ,String oview ,String urate ,String id ,String rdate){
        imgUrl = url;
        this.title = title;
        overview = oview;
        user_rating = urate;
        this.id = id;
        release_date =rdate;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public MovieItem(){
        super();
    }

    public String getPosterPath(){
        return imgUrl;
    }

    public  void setPosterPath(String imgUrl) {this.imgUrl =  imgUrl;}

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getUser_rating() {
        return user_rating;
    }

    public String getRelease_date() {
        return release_date;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgUrl);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(user_rating);
        dest.writeString(id);
        dest.writeString(release_date);
    }

    public static final Parcelable.Creator<MovieItem> CREATOR
            = new Parcelable.Creator<MovieItem>() {
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        public MovieItem[] newArray(int size) {
            return new MovieItem [size];
        }
    };

    private MovieItem(Parcel in) {
        imgUrl = in.readString();
        title = in.readString();
        overview = in.readString();
        user_rating = in.readString();
        id = in.readString();
        release_date = in.readString();
    }
}
