package ru.artempugachev.popularmovies.movielist

import io.reactivex.Observable
import ru.artempugachev.popularmovies.movielist.api.Movie

/**
 * Cache movie data in memory
 * */
interface Repository {
    fun getPopularMoviesFromMemory(): Observable<Movie>
    fun getPopularMoviesFromNetwork(): Observable<Movie>

    fun getPopularMovies(): Observable<Movie>
}