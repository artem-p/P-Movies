package ru.artempugachev.popularmovies.moviedetails

import android.content.Intent
import io.reactivex.disposables.Disposable
import ru.artempugachev.popularmovies.movielist.api.Movie

class MovieDetailsPresenterImpl : MovieDetailsMvpContract.Presenter {
    private var view: MovieDetailsMvpContract.View? = null
    private var movie: Movie? = null
    private var trailersSubscription: Disposable? = null
    private var reviewsSubscription: Disposable? = null


    override fun getMovieFromIntent(intent: Intent): Movie {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadTrailers(movie: Movie) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadReviews(movie: Movie) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bindView(view: MovieDetailsMvpContract.View) {
        this.view = view
    }

    override fun insubscribeData() {
        if (trailersSubscription?.isDisposed == false) {
            trailersSubscription?.dispose()
        }

        if (reviewsSubscription?.isDisposed == false) {
            reviewsSubscription?.dispose()
        }
    }
}