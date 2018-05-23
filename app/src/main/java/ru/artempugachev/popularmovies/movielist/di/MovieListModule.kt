package ru.artempugachev.popularmovies.movielist.di

import dagger.Module
import dagger.Provides
import ru.artempugachev.popularmovies.movielist.*
import ru.artempugachev.popularmovies.tmdb.TmdbApiInterface
import javax.inject.Singleton

@Module
class MovieListModule {
    @Provides
    fun providePresenter(model: MovieListMvpContract.Model): MovieListMvpContract.Presenter {
        return MovieListPresenterImpl(model)
    }


    @Provides
    fun provideModel(repository: Repository): MovieListMvpContract.Model {
        return MovieModelImpl(repository)
    }


    @Singleton
    @Provides
    fun provideRepository(tmdbApiInterface: TmdbApiInterface): Repository {
        return RepositoryImpl(tmdbApiInterface)
    }
}