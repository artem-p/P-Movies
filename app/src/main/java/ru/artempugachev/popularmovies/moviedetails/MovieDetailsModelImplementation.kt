package ru.artempugachev.popularmovies.moviedetails

import io.reactivex.Observable
import ru.artempugachev.popularmovies.moviedetails.api.Review
import ru.artempugachev.popularmovies.moviedetails.api.Video
import ru.artempugachev.popularmovies.movielist.api.Movie


/**
 * For now, this class just passes calls to the repository.
 * We can add some data formatting here.
 * */
class MovieDetailsModelImplementation(private val repository: DetailsRepository): MovieDetailsMvpContract.Model {
    override fun getMovie(): Movie {
        return repository.getMovie()
    }

    override fun getTrailers(): Observable<Video> {
        return repository.getTrailers()
    }

    override fun getReviews(): Observable<Review> {
        return repository.getReviews()
    }
}
