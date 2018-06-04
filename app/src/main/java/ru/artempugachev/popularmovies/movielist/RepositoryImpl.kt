package ru.artempugachev.popularmovies.movielist

import io.reactivex.Observable
import ru.artempugachev.popularmovies.movielist.api.Movie
import ru.artempugachev.popularmovies.movielist.api.TmdbResponse
import ru.artempugachev.popularmovies.tmdb.TmdbApiInterface


/**
 * Implement {@link Repository}
 * */
class `RepositoryImpl(val tmdbApiInterface: TmdbApiInterface): Repository {
    override fun getMovies(sort: String, page: Int): Observable<Movie> {
        val moviesObservable = tmdbApiInterface.getMovies(sort, page)

        // extract movies from response
        // use concat map to preserve order
        return moviesObservable.concatMap {
            tmdbResponse: TmdbResponse -> Observable.fromIterable(tmdbResponse.results)
        }
    }
}