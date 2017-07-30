package ru.artempugachev.popularmovies.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Data class for review response
 */

public class ReviewResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<Review> results;

    public List<Review> getResults() {
        return results;
    }
}
