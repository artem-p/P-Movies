package ru.artempugachev.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.artempugachev.popularmovies.model.Video;

/**
 * Data class for video response
 */

public class VideoResponse {
    @SerializedName("id")
    private String id;


    @SerializedName("results")
    private List<Video> results;

    public List<Video> getResults() {
        return results;
    }
}
