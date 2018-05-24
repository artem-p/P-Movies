package ru.artempugachev.popularmovies.movielist

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import ru.artempugachev.popularmovies.movielist.api.Movie

class MovieListPresenterImpl(private val model: MovieListMvpContract.Model) : MovieListMvpContract.Presenter {
    private var subscription: Disposable? = null
    private var view: MovieListMvpContract.View? = null


    /**
     * Load movies from Tmdb
     * */
    override fun loadMovies(sort: String, page: Int) {
        subscription = model.getMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Movie>() {
                    override fun onNext(movie: Movie) {
                        view?.updateMovies(movie)
                    }


                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        view?.showErrorLoadingMovies()
                    }


                    override fun onComplete() {

                    }
                })
    }


    override fun bindView(view: MovieListMvpContract.View) {
        this.view = view
    }


    override fun unsubscribeRx() {
        if (subscription?.isDisposed == false) {
            subscription?.dispose()
        }
    }
}