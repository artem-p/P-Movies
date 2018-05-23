package ru.artempugachev.popularmovies.tmdb;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.artempugachev.popularmovies.movielist.api.MovieResponse;
import ru.artempugachev.popularmovies.movielist.api.ReviewResponse;
import ru.artempugachev.popularmovies.movielist.api.VideoResponse;

/**
 * Retrofit TMDB API Interface
 */

public interface TmdbApiInterface {
    @GET("movie/{sort}")
    Call<MovieResponse> getMovies(@Path("sort") String sortOrder,
                                  @Query("page") int pageNumber);

    @GET("movie/{id}/videos")
    Observable<VideoResponse> getVideos(@Path("id") String id);

    @GET("movie/{id}/reviews")
    Call<ReviewResponse> getReviews(@Path("id") String id);
}
