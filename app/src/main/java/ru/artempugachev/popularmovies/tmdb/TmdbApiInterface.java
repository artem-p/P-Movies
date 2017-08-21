package ru.artempugachev.popularmovies.tmdb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.artempugachev.popularmovies.model.MovieResponse;
import ru.artempugachev.popularmovies.model.ReviewResponse;
import ru.artempugachev.popularmovies.model.VideoResponse;

/**
 * Retrofit TMDB API Interface
 */

public interface TmdbApiInterface {
    @GET("movie/{sort}")
    Call<MovieResponse> getMovies(@Path("sort") String sortOrder, @Query("api_key") String apiKey,
                                  @Query("page") int pageNumber);

    @GET("movie/{id}/videos")
    Call<VideoResponse> getVideos(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewResponse> getReviews(@Path("id") String id, @Query("api_key") String apiKey);
}
