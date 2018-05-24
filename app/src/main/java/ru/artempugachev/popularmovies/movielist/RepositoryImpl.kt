package ru.artempugachev.popularmovies.movielist

import io.reactivex.Observable
import ru.artempugachev.popularmovies.movielist.api.Movie
import ru.artempugachev.popularmovies.tmdb.TmdbApiInterface

class RepositoryImpl(tmdbApiInterface: TmdbApiInterface): Repository {
    private val lastUpdateTime = System.currentTimeMillis()
    private val popularMovies = ArrayList<Movie>()
    private val topRatedMovies = ArrayList<Movie>()


    /**
     * Check if cache is up to date
     * */
    private fun isUpToDate(): Boolean {
        return System.currentTimeMillis() - lastUpdateTime < VALID_TIME
    }


    override fun getPopularMoviesFromMemory(): Observable<List<Movie>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTopRatedMoviesFromMemory(): Observable<List<Movie>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPopularMoviesFromNetwork(): Observable<List<Movie>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTopRatedMoviesFromNetwork(): Observable<List<Movie>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPopularMovies(): Observable<List<Movie>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTopRatedMovies(): Observable<List<Movie>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    companion object {
        const val VALID_TIME = 60 * 1000    // time after cache expired
    }
}