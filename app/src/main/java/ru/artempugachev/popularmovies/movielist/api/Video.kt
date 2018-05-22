package ru.artempugachev.popularmovies.movielist.api

import com.google.gson.annotations.SerializedName

/**
 * Object to store video information from TMDB json response
 */

class Video {
    @SerializedName("id")
    private val id: String? = null

    @SerializedName("name")
    val name: String? = null

    @SerializedName("type")
    private val type: String? = null

    @SerializedName("key")
    private val youtubeId: String? = null
    
    private val THUMBNAIL_URL_FORMAT = "http://img.youtube.com/vi/%s/mqdefault.jpg"
    private val TRAILER_URL_FORMAT = "https://www.youtube.com/watch?v=%s"


    /**
     * Check if the video is trailer
     */
    fun isTrailer(): Boolean {
        return type == TRAILER_TAG
    }


    /**
     * Get url for trailer thumbnail
     */
    val thumbnailUrl: String
        get() = String.format(THUMBNAIL_URL_FORMAT, youtubeId)

    val videoUrl: String
        get() = String.format(TRAILER_URL_FORMAT, youtubeId)


    companion object {
        private val TRAILER_TAG = "Trailer"
    }
}
