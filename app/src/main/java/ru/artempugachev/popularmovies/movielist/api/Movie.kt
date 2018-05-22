package ru.artempugachev.popularmovies.movielist.api

import android.content.ContentValues
import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName

import ru.artempugachev.popularmovies.data.MovieContract

/**
 * Object for store movie information. Corresponds to TMDB JSON.
 */

class Movie : Parcelable {
    @SerializedName("poster_path")
    private var posterPath: String? = null

    @SerializedName("overview")
    var overview: String? = null

    @SerializedName("release_date")
    var releaseDate: String? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("backdrop_path")
    private var backdropPath: String? = null

    @SerializedName("vote_average")
    private var voteAverage: Double? = null

    val fullPosterPath: String
        get() = BASE_IMAGE_URL + posterPath!!

    val fullBackdropPath: String
        get() = BASE_IMAGE_URL + backdropPath!!

    val rating: String
        get() = voteAverage!!.toString()


    constructor(posterPath: String, overview: String, releaseDate: String, id: String,
                title: String, backdropPath: String, voteAverage: Double?) {
        this.posterPath = posterPath
        this.overview = overview
        this.releaseDate = releaseDate
        this.id = id
        this.title = title
        this.backdropPath = backdropPath
        this.voteAverage = voteAverage
    }


    fun toContentValues(): ContentValues {
        val cv = ContentValues()

        cv.put(MovieContract.MovieEntry.MOVIE_ID, this.id)
        cv.put(MovieContract.MovieEntry.TITLE, this.title)
        cv.put(MovieContract.MovieEntry.RELEASE_DATE, this.releaseDate)
        cv.put(MovieContract.MovieEntry.OVERVIEW, this.overview)
        cv.put(MovieContract.MovieEntry.POSTER_PATH, this.posterPath)
        cv.put(MovieContract.MovieEntry.BACKDROP_PATH, this.backdropPath)
        cv.put(MovieContract.MovieEntry.VOTE_AVERAGE, this.voteAverage)

        return cv
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeString(id)
        out.writeString(title)
        out.writeString(releaseDate)
        out.writeDouble(voteAverage!!)
        out.writeString(overview)
        out.writeString(posterPath)
        out.writeString(backdropPath)
    }

    constructor(parcel: Parcel) {
        id = parcel.readString()
        title = parcel.readString()
        releaseDate = parcel.readString()
        voteAverage = parcel.readDouble()
        overview = parcel.readString()
        posterPath = parcel.readString()
        backdropPath = parcel.readString()
    }

    companion object CREATOR: Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}

private val BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w342/"
