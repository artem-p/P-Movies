package ru.artempugachev.popularmovies.movielist

import io.reactivex.Observable
import ru.artempugachev.popularmovies.movielist.MovieListActivity.Companion.SORT_ORDER_POPULAR
import ru.artempugachev.popularmovies.movielist.api.Movie
import ru.artempugachev.popularmovies.movielist.api.MovieResponse
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
    override fun getPopularMoviesFromMemory(): Observable<List<Movie>> {
        return if (isUpToDate()) {
            Observable.just(popularMovies)
        } else {
            lastUpdateTime = System.currentTimeMillis()
            popularMovies.clear()
            Observable.empty()
        }
    }



    override fun getPopularMoviesFromNetwork(): Observable<List<Movie>> {
        val popularObservable = tmdbApiInterface.getMovies(SORT_ORDER_POPULAR, 1)

        return popularObservable.concatMap {
            tmdbResponse: MovieResponse -> Observable.just(tmdbResponse.results)
        }.doOnNext {
//            popularMovies.
        }
    }


    override fun getPopularMovies(): Observable<List<Movie>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    companion object {
        const val VALID_TIME = 60 * 1000    // time after cache expired
    }
}