package ru.artempugachev.popularmovies.di

import android.content.Context
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides

/**
 * Picasso dependency module
 * */
@Module(includes = [(ContextModule::class)])
class PicassoModule {
    @Provides
    fun picasso(context: Context): Picasso {
        return Picasso.Builder(context)
                .build()
    }
}