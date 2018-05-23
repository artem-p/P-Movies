package ru.artempugachev.popularmovies.movielist

import io.reactivex.Observable
import ru.artempugachev.popularmovies.movielist.api.Movie

class RepositoryImpl: Repository {
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
}