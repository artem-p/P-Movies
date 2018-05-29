package ru.artempugachev.popularmovies.moviedetails.api

import io.reactivex.Observable
import ru.artempugachev.popularmovies.movielist.api.Movie


interface MovieDetailsInteractor {
    fun getTrailers(movie: Movie): Observable<List<Video>>
    fun getReviews(movie: Movie): Observable<List<Review>>
}