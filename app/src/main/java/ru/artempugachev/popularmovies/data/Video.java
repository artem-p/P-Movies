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

    private final static String TRAILER_TAG = "Trailer";


    /**
     * Check if the video is trailer
     * */
    public boolean isTrailer() {
        return type.equals(TRAILER_TAG);
    }

    public String getName() {
        return name;
    }
}
