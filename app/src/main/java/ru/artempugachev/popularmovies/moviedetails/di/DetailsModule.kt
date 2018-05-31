package ru.artempugachev.popularmovies.moviedetails.di

import dagger.Module
import dagger.Provides
import ru.artempugachev.popularmovies.moviedetails.*
import ru.artempugachev.popularmovies.movielist.api.Movie
import ru.artempugachev.popularmovies.tmdb.TmdbApiInterface


@Module
class DetailsModule {
    @Provides
    fun providePresenter(model: MovieDetailsMvpContract.Model): MovieDetailsMvpContract.Presenter {
        return MovieDetailsPresenterImpl(model)
    }


    @Provides
    fun provideModel(repository: DetailsRepository): MovieDetailsMvpContract.Model {
        return MovieDetailsModelImplementation(repository)
    }


    @Provides
    fun provideRepository(movie: Movie, tmdbApiInterface: TmdbApiInterface): DetailsRepositoryImpl {
        return DetailsRepositoryImpl(movie, tmdbApiInterface)
    }
}
