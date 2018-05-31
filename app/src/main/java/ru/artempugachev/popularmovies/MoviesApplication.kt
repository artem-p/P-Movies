package ru.artempugachev.popularmovies

import android.app.Application

import com.facebook.stetho.Stetho
import ru.artempugachev.popularmovies.di.ContextModule
import ru.artempugachev.popularmovies.di.DaggerMovieComponent
import ru.artempugachev.popularmovies.di.MovieComponent
import ru.artempugachev.popularmovies.moviedetails.di.DetailsComponent
import ru.artempugachev.popularmovies.moviedetails.di.DetailsModule
import ru.artempugachev.popularmovies.movielist.di.DaggerMovieListComponent
import ru.artempugachev.popularmovies.movielist.di.MovieListComponent
import ru.artempugachev.popularmovies.movielist.di.MovieListModule
import ru.artempugachev.popularmovies.tmdb.TmdbModule


class MoviesApplication : Application() {
    private lateinit var movieComponent: MovieComponent
    private lateinit var movieListComponent: MovieListComponent
    private lateinit var movieDetailsComponent: DetailsComponent

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)

        movieListComponent = DaggerMovieListComponent.builder()
                .tmdbModule(TmdbModule(BuildConfig.TMDB_API_KEY))
                .movieListModule(MovieListModule())
                .build()

        movieComponent = DaggerMovieComponent.builder()
                .contextModule(ContextModule(this))
                .build()

        movieDetailsComponent = DaggerDetailsComponent.builder()
                .tmdbModule(TmdbModule(BuildConfig.TMDB_API_KEY))
                .detailsModule(DetailsModule())
                .build()
    }


    fun getMovieListComponent(): MovieListComponent {
        return movieListComponent
    }


    fun getMovieDetailsComponent(): DetailsComponent {
        return movieDetailsComponent
    }


    fun getMovieComponent(): MovieComponent {
        return movieComponent
    }
}
