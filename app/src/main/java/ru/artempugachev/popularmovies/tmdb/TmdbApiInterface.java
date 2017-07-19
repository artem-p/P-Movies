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
    @GET("movie/{sort}")
    Call<MoviesResponse> getMovies(@Path("sort") String sortOrder, @Query("api_key") String apiKey);
}
