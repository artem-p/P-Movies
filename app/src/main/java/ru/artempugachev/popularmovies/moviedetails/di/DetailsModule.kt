package ru.artempugachev.popularmovies.moviedetails.di

import dagger.Module
import dagger.Provides
import ru.artempugachev.popularmovies.moviedetails.MovieDetailsMvpContract
import ru.artempugachev.popularmovies.moviedetails.MovieDetailsPresenterImpl


@Module
class DetailsModule {

    @Provides
    fun providePresenter(model: MovieDetailsMvpContract.Model): MovieDetailsMvpContract.Presenter {
        return MovieDetailsPresenterImpl(model)
    }



}