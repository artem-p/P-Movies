package ru.artempugachev.popularmovies.moviedetails.api

import com.google.gson.annotations.SerializedName

import ru.artempugachev.popularmovies.moviedetails.api.Video

/**
 * Data class for video response
 */

data class VideoResponse (
    @SerializedName("id")
    val id: String,

    @SerializedName("results")
    val results: List<Video>
)
