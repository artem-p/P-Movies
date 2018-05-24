package ru.artempugachev.popularmovies.movielist.api

import com.google.gson.annotations.SerializedName

import ru.artempugachev.popularmovies.movielist.api.Movie

/**
 * Data class for Movie json response.
 */

data class TmdbResponse (
        @SerializedName("page")
    var page: Int,

        @SerializedName("results")
    var results: List<Movie>?,

        @SerializedName("total_results")
    var totalResults: Int,

        @SerializedName("total_pages")
    var totalPages: Int
)
