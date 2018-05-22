package ru.artempugachev.popularmovies.movielist

import io.reactivex.Observable
import ru.artempugachev.popularmovies.movielist.api.Movie

/**
 * Contract for movie list MVP
 * */

interface MovieListMvpContract {
    /**
     * Define functions we exposed from the view layer
     * */
    interface View {
        fun updateMovies(movies: List<Movie>)
        fun showMovies()
        fun showProgress()
        fun showMovieDetail()
    }


    interface Presenter {
        fun bindView(view: View)
        fun loadMovies()
        fun unsubscribeRx()
    }


    interface Model {
        fun getMovies(): Observable<List<Movie>>
    }


    interface UserActionListener {
        fun openMovieDetails()
    }
}
