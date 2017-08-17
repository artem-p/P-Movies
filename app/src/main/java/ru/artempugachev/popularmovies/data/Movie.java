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
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("genre_ids")
    private List<Integer> genreIds = new ArrayList<Integer>();
    @SerializedName("id")
    private String id;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("title")
    private String title;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("popularity")
    private Double popularity;
    @SerializedName("vote_count")
    private Integer voteCount;
    @SerializedName("video")
    private Boolean video;
    @SerializedName("vote_average")
    private Double voteAverage;

    private final static String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w342/";

    public Movie(String posterPath, boolean adult, String overview, String releaseDate, List<Integer> genreIds, String id,
                 String originalTitle, String originalLanguage, String title, String backdropPath, Double popularity,
                 Integer voteCount, Boolean video, Double voteAverage) {
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.genreIds = genreIds;
        this.id = id;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
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


    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
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


    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
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
