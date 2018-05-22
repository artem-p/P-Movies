package ru.artempugachev.popularmovies.movielist.api

import com.google.gson.annotations.SerializedName

import ru.artempugachev.popularmovies.movielist.api.Review

/**
 * Data class for review response
 */

data class ReviewResponse (
    @SerializedName("id")
    val id: String,

    @SerializedName("page")
    val page: Int,

    @SerializedName("results")
    val results: List<Review>
)
