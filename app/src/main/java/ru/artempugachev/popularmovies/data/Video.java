package ru.artempugachev.popularmovies.data;

import com.google.gson.annotations.SerializedName;

/**
 * Object to store video information from TMDB json response
 */

public class Video {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("key")
    private String youtubeId;

    private final static String TRAILER_TAG = "Trailer";
    private final String TRAILER_URL_FORMAT = "http://img.youtube.com/vi/%s/mqdefault.jpg";


    /**
     * Check if the video is trailer
     * */
    public boolean isTrailer() {
        return type.equals(TRAILER_TAG);
    }

    public String getName() {
        return name;
    }

    /**
     * Get url for trailer thumbnail
     * */
    public String getThumbnailUrl() {
        return String.format(TRAILER_URL_FORMAT, youtubeId);
    }
}
