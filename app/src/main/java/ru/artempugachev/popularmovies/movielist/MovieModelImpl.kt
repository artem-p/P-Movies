package ru.artempugachev.popularmovies.movielist

import io.reactivex.Observable
import ru.artempugachev.popularmovies.movielist.api.Movie

class MovieModelImpl(private val repository: Repository) : MovieListMvpContract.Model {
    override fun getMovies(): Observable<List<Movie>> {
        return repository.getPopularMovies()
    }
}
