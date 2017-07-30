package ru.artempugachev.popularmovies.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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
