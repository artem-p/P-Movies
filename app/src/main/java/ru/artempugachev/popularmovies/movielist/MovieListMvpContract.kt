package ru.artempugachev.popularmovies.movielist

/**
 * Contract for movie list MVP
 * */

interface MovieListMvpContract {
    /**
     * Define functions we exposed from the view layer
     * */
    interface View {
        fun showMovies()
        fun showProgress()
        fun showMovieDetail()
    }


    interface UserActionListener {
        fun openMovieDetails()
    }
}
