package ru.artempugachev.popularmovies.data;

import com.google.gson.annotations.SerializedName;

/**
 * Data class for reviews
 */

public class Review {
    @SerializedName("id")
    private String id;

    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;

    @SerializedName("url")
    private String url;

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
