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
        fun updateMovies(movie: Movie)
        fun showMovies()
        fun showProgress()
        fun showMovieDetail()
        fun showErrorLoadingMovies()
    }


    interface Presenter {
        fun bindView(view: View)

        /**
         * Load movies from Tmdb
         * @param sort How movies are sorted: popular or top rated
         * @param page Page number
         * */
        fun loadMovies(sort: String, page: Int)

        fun unsubscribeRx()
    }


    interface Model {
        fun getMovies(): Observable<Movie>
    }


    interface UserActionListener {
        fun openMovieDetails()
    }
}
