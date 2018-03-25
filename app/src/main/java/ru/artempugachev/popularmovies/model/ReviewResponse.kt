package ru.artempugachev.popularmovies.model

import com.google.gson.annotations.SerializedName

import ru.artempugachev.popularmovies.model.Review

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
