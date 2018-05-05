package ru.artempugachev.popularmovies.movielist

/**
 * Contract for movie list MVP
 * */

interface MovieListContract {
    /**
     * Define functions we exposed from the view layer
     * */
    interface View {
        fun showMovies()
        fun showProgress()
        fun showMovieDetail()
    }
}
