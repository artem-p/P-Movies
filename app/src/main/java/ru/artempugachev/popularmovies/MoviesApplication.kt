package ru.artempugachev.popularmovies

import android.app.Application

import com.facebook.stetho.Stetho
import ru.artempugachev.popularmovies.di.ContextModule
import ru.artempugachev.popularmovies.di.DaggerMovieComponent
import ru.artempugachev.popularmovies.di.MovieComponent


class MoviesApplication : Application() {
    private lateinit var movieComponent: MovieComponent

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)

        movieComponent = DaggerMovieComponent.builder()
                .contextModule(ContextModule(this))
                .build()
    }


    fun getComponent(): MovieComponent {
        return movieComponent
    }
}
