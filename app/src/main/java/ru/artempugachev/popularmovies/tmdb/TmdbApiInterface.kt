package ru.artempugachev.popularmovies.tmdb

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.artempugachev.popularmovies.movielist.api.TmdbResponse
import ru.artempugachev.popularmovies.movielist.api.ReviewResponse
import ru.artempugachev.popularmovies.movielist.api.VideoResponse

/**
 * Retrofit TMDB API Interface
 */

interface TmdbApiInterface {
    @GET("movie/{sort}")
    fun getMovies(@Path("sort") sortOrder: String,
                  @Query("page") pageNumber: Int): Observable<TmdbResponse>

    @GET("movie/{id}/videos")
    fun getVideos(@Path("id") id: String): Observable<VideoResponse>

    @GET("movie/{id}/reviews")
    fun getReviews(@Path("id") id: String): Call<ReviewResponse>
}
