package ru.artempugachev.popularmovies.moviedetails.di

import dagger.Component
import ru.artempugachev.popularmovies.moviedetails.MovieDetailsActivity
import ru.artempugachev.popularmovies.tmdb.TmdbModule
import javax.inject.Singleton

@Singleton
@Component (modules = [TmdbModule::class,
    DetailsModule::class])

interface DetailsComponent {
    fun inject(target: MovieDetailsActivity)
}
