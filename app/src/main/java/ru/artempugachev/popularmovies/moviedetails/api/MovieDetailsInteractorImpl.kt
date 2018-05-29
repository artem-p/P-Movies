package ru.artempugachev.popularmovies.moviedetails.api

import io.reactivex.Observable
import ru.artempugachev.popularmovies.movielist.api.Movie
import ru.artempugachev.popularmovies.tmdb.TmdbApiInterface

class MovieDetailsInteractorImpl(private val tmdbApiInterface: TmdbApiInterface): MovieDetailsInteractor {
    override fun getTrailers(movie: Movie): Observable<List<Video>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getReviews(movie: Movie): Observable<List<Review>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}