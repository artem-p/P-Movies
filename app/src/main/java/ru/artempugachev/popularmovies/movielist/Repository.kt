package ru.artempugachev.popularmovies.movielist

import io.reactivex.Observable
import ru.artempugachev.popularmovies.movielist.api.Movie

/**
 * For now, just pass query to network.
 * We can add memory or db cache here.
 * */
interface Repository {
    fun getMovies(sort: String, page: Int): Observable<Movie>
}

