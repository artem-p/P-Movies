package ru.artempugachev.popularmovies.di

import android.content.Context
import dagger.Module
import dagger.Provides

/**
 * Provides context
 * */
@Module
class ContextModule(val context: Context) {

    @Provides
    fun context(): Context {
        return context.applicationContext
    }
}