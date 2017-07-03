package ru.artempugachev.popularmovies.tmdb;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.artempugachev.popularmovies.data.MoviesResponse;

/**
 * Retrofit TMDB API Interface
 */

public interface TmdbApiInterface {
    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MoviesResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}
