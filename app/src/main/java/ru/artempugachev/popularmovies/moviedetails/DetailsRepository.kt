package ru.artempugachev.popularmovies.moviedetails

import io.reactivex.Observable
import ru.artempugachev.popularmovies.moviedetails.api.Review
import ru.artempugachev.popularmovies.moviedetails.api.Video
import ru.artempugachev.popularmovies.movielist.api.Movie


interface DetailsRepository {
    fun getMovie(): Movie
    fun getTrailers(): Observable<Video>
    fun getReviews(): Observable<Review>
}