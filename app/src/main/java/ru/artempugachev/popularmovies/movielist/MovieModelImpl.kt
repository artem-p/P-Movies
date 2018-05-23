package ru.artempugachev.popularmovies.movielist

import io.reactivex.Observable
import ru.artempugachev.popularmovies.movielist.api.Movie

class MovieModelImpl : MovieListMvpContract.Model {
    override fun getMovies(): Observable<List<Movie>> {
        return Observable.just(arrayListOf())
    }

}
