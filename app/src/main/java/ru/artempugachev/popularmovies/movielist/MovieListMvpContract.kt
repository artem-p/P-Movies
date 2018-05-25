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
        fun showProgress()
        fun hideProgress()
        fun showMovieDetail()
        fun showErrorLoadingMovies()

        /**
         * Empty movies list
         * */
        fun emptyMovies()
    }


    interface Presenter {
        fun bindView(view: View)

        /**
         * Load movies from Tmdb
         * @param sort How movies are sorted: popular or top rated
         * @param page Page number
         * */
        fun loadMovies(sort: String, page: Int)


        /**
         * We implement endless scroll
         * Call this method when need to load more movies
         * @param page Page number
         * */
        fun loadMore(page: Int)

        fun sortOrderChange(newSortOrder: String)

        fun unsubscribeRx()
    }


    interface Model {
        fun getMovies(sort: String, page: Int): Observable<Movie>
    }


    interface UserActionListener {
        fun openMovieDetails()
    }
}
