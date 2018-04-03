package ru.artempugachev.popularmovies.di

import android.content.Context
import dagger.Module
import dagger.Provides

/**
 * Provides context
 * */
@Module
class ContextModule(context: Context) {
    private lateinit var context: Context

    @Provides
    fun context(): Context {
        return context.applicationContext
    }
}