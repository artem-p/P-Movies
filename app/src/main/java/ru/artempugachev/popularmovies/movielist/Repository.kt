package ru.artempugachev.popularmovies.movielist

import io.reactivex.Observable
import ru.artempugachev.popularmovies.movielist.api.Movie

/**
 * Cache movie data in memory
 * */
interface Repository {
    fun getPopularMoviesFromMemory(): Observable<List<Movie>>
    fun getTopRatedMoviesFromMemory(): Observable<List<Movie>>
    fun getPopularMoviesFromNetwork(): Observable<List<Movie>>
    fun getTopRatedMoviesFromNetwork(): Observable<List<Movie>>

    fun getPopularMovies(): Observable<List<Movie>>
    fun getTopRatedMovies(): Observable<List<Movie>>
}