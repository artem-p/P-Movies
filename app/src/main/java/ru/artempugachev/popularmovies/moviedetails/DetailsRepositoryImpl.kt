package ru.artempugachev.popularmovies.moviedetails

import io.reactivex.Observable
import ru.artempugachev.popularmovies.moviedetails.api.Review
import ru.artempugachev.popularmovies.moviedetails.api.Video
import ru.artempugachev.popularmovies.movielist.api.Movie
import ru.artempugachev.popularmovies.tmdb.TmdbApiInterface

class DetailsRepositoryImpl(private val movie: Movie,
                            private val tmdbApiInterface: TmdbApiInterface): DetailsRepository {
    override fun getMovie(): Movie {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTrailers(): Observable<Video> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getReviews(): Observable<Review> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}