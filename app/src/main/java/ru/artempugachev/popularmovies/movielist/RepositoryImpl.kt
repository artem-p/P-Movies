package ru.artempugachev.popularmovies.movielist

import io.reactivex.Observable
import ru.artempugachev.popularmovies.movielist.MovieListActivity.Companion.SORT_ORDER_POPULAR
import ru.artempugachev.popularmovies.movielist.api.Movie
import ru.artempugachev.popularmovies.movielist.api.TmdbResponse
import ru.artempugachev.popularmovies.tmdb.TmdbApiInterface

class RepositoryImpl(val tmdbApiInterface: TmdbApiInterface): Repository {
    private var lastUpdateTime = System.currentTimeMillis()
    private var popularMovies = ArrayList<Movie>()


    /**
     * Check if cache is up to date
     * */
    private fun isUpToDate(): Boolean {
        return System.currentTimeMillis() - lastUpdateTime < VALID_TIME
    }


    /**
     * Return cached popular movies
     * */
    override fun getPopularMoviesFromMemory(): Observable<Movie> {
        return if (isUpToDate()) {
            Observable.fromIterable(popularMovies)
        } else {
            lastUpdateTime = System.currentTimeMillis()
            popularMovies.clear()
            Observable.empty()
        }
    }


    override fun getPopularMoviesFromNetwork(): Observable<Movie> {
        val popularObservable = tmdbApiInterface.getMovies(SORT_ORDER_POPULAR, 1)

        // extract movies from response
        // use concat map to preserve order
        return popularObservable.concatMap {
            tmdbResponse: TmdbResponse -> Observable.fromIterable(tmdbResponse.results)
        }.doOnNext {
            // save movies to the cache
            movie: Movie -> popularMovies.add(movie)
        }
    }


    override fun getPopularMovies(): Observable<Movie> {
        return getPopularMoviesFromMemory().switchIfEmpty(getPopularMoviesFromNetwork())
    }


    companion object {
        const val VALID_TIME = 60 * 1000    // time after cache expired
    }
}