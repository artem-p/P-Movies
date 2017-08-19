package ru.artempugachev.popularmovies.data;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Object for store movie information. Corresponds to TMDB JSON.
 */

public class Movie implements Parcelable{
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("vote_average")
    private Double voteAverage;

    private final static String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w342/";

    public Movie(String posterPath, String overview, String releaseDate, String id,
                 String title, String backdropPath,  Double voteAverage) {
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
        this.title = title;
        this.backdropPath = backdropPath;
        this.voteAverage = voteAverage;
    }

    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();

        cv.put(MovieContract.MovieEntry.MOVIE_ID, this.id);
        cv.put(MovieContract.MovieEntry.TITLE, this.title);
        cv.put(MovieContract.MovieEntry.RELEASE_DATE, this.releaseDate);
        cv.put(MovieContract.MovieEntry.OVERVIEW, this.overview);
        cv.put(MovieContract.MovieEntry.POSTER_PATH, this.posterPath);
        cv.put(MovieContract.MovieEntry.BACKDROP_PATH, this.backdropPath);
        cv.put(MovieContract.MovieEntry.VOTE_AVERAGE, this.voteAverage);

        return cv;
    }

    public String getOverview() {
        return overview;
    }


    public String getReleaseDate() {
        return releaseDate;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }


    public String getFullPosterPath() {
        return BASE_IMAGE_URL + posterPath;
    }

    public String getFullBackdropPath() {
        return BASE_IMAGE_URL + backdropPath;
    }


    public String getRating() {
        return voteAverage.toString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(title);
        out.writeString(releaseDate);
        out.writeDouble(voteAverage);
        out.writeString(overview);
        out.writeString(posterPath);
        out.writeString(backdropPath);
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie (Parcel in) {
        id = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readDouble();
        overview = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
    }
}
