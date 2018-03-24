package ru.artempugachev.popularmovies.model

import com.google.gson.annotations.SerializedName

import ru.artempugachev.popularmovies.model.Movie

/**
 * Data class for Movie json response. We need it as it have extra fields (like page number) that
 * we don't want in Movie data object. See there: http://www.androidhive.info/2016/05/android-working-with-retrofit-http-library/
 */

data class MovieResponse (
    @SerializedName("page")
    var page: Int,

    @SerializedName("results")
    var results: List<Movie>?,

    @SerializedName("total_results")
    var totalResults: Int,

    @SerializedName("total_pages")
    var totalPages: Int
)
