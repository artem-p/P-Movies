package ru.artempugachev.popularmovies.model

import com.google.gson.annotations.SerializedName

import ru.artempugachev.popularmovies.model.Video

/**
 * Data class for video response
 */

data class VideoResponse (
    @SerializedName("id")
    val id: String,

    @SerializedName("results")
    val results: List<Video>
)
