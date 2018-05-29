package ru.artempugachev.popularmovies.moviedetails

import android.content.Intent
import ru.artempugachev.popularmovies.movielist.api.Movie
import ru.artempugachev.popularmovies.moviedetails.api.Review
import ru.artempugachev.popularmovies.moviedetails.api.Video


/**
 * In movie details we display information about single movie
 * */
interface MovieDetailsMvpContract {
    interface View {
        fun showDetails(movie: Movie)
        fun showTrailers(trailers: List<Video>)
        fun showReviews(reviews: List<Review>)

        fun showTrailersError()
        fun showReviewsError()
    }


    interface Presenter {
        fun getMovieFromIntent(intent: Intent)
        fun loadTrailers(movie: Movie)
        fun loadReviews(movie: Movie)

        fun bindView(view: View)
        fun insubscribeData()
    }


    /**
     * Store movie in the model
     * */
    interface Model {
        fun setMovie(movie: Movie)
        fun getMovie(): Movie
    }
}