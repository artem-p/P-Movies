package ru.artempugachev.popularmovies.moviedetails

import ru.artempugachev.popularmovies.moviedetails.api.Review
import ru.artempugachev.popularmovies.moviedetails.api.Video
import ru.artempugachev.popularmovies.movielist.api.Movie


interface Repository {
    fun getMovie(): Movie
    fun getTrailers(): List<Video>
    fun getReviews(): List<Review>
}