package ru.artempugachev.popularmovies.movielist

import io.reactivex.Observable
import ru.artempugachev.popularmovies.movielist.api.Movie

class MovieModelImpl(private val repository: Repository) : MovieListMvpContract.Model {
    override fun getMovies(sort: String, page: Int): Observable<Movie> {
        return repository.getMovies(sort, page)
    }
}
