package ru.artempugachev.popularmovies.moviedetails.api

import com.google.gson.annotations.SerializedName

/**
 * Data class for reviews
 */

class Review (
    @SerializedName("id")
    val id: String,

    @SerializedName("author")
    val author: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("url")
    val url: String
)
