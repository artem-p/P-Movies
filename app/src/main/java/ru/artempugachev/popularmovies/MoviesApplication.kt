package ru.artempugachev.popularmovies

import android.app.Application

import com.facebook.stetho.Stetho
import ru.artempugachev.popularmovies.di.ContextModule
import ru.artempugachev.popularmovies.di.DaggerMovieComponent
import ru.artempugachev.popularmovies.di.MovieComponent
import ru.artempugachev.popularmovies.movielist.di.MovieListComponent


class MoviesApplication : Application() {
    private lateinit var movieComponent: MovieComponent
    private lateinit var movieListComponent: MovieListComponent

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)

        movieListComponent = DaggerMovieListComponent.builder()
                .tmdbModule(BuildConfig.TMDB_API_KEY)
                .movieListModule()
                .build()

        movieComponent = DaggerMovieComponent.builder()
                .contextModule(ContextModule(this))
                .build()
    }


    fun getMovieListComponent(): MovieListComponent {
        return movieListComponent
    }


    fun getMovieComponent(): MovieComponent {
        return movieComponent
    }
}
