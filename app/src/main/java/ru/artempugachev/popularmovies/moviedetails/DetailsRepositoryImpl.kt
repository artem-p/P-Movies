package ru.artempugachev.popularmovies.moviedetails

import io.reactivex.Observable
import ru.artempugachev.popularmovies.moviedetails.api.Review
import ru.artempugachev.popularmovies.moviedetails.api.ReviewResponse
import ru.artempugachev.popularmovies.moviedetails.api.Video
import ru.artempugachev.popularmovies.moviedetails.api.VideoResponse
import ru.artempugachev.popularmovies.movielist.api.Movie
import ru.artempugachev.popularmovies.tmdb.TmdbApiInterface


/**
 * Simple repository.
 * We can add caching later here.
 * */
class DetailsRepositoryImpl(private val movie: Movie,
                            private val tmdbApiInterface: TmdbApiInterface): DetailsRepository {
    override fun getMovie(): Movie {
        return movie
    }

    override fun getTrailers(): Observable<Video> {
        val trailersObservable = Observable.empty<Video>()

        movie.id?.let {
            return tmdbApiInterface.getVideos(it).concatMap {
                trailerResponse: VideoResponse -> Observable.fromIterable(trailerResponse.results)
            }
        }

        return trailersObservable
    }

    override fun getReviews(): Observable<Review> {
        val reviewsObservable = Observable.empty<Review>()

        movie.id?.let {
            return tmdbApiInterface.getReviews(it).concatMap {
                reviewResponse: ReviewResponse -> Observable.fromIterable(reviewResponse.results)
            }
        }

        return reviewsObservable
    }
}