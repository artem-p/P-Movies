package ru.artempugachev.popularmovies.movielist.di

import dagger.Component
import ru.artempugachev.popularmovies.movielist.MovieListActivity
import ru.artempugachev.popularmovies.tmdb.TmdbModule
import javax.inject.Singleton

@Singleton
@Component(modules = [TmdbModule::class, MovieListModule::class])
interface MovieListComponent {
    fun inject(target: MovieListActivity)
}