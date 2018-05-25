package ru.artempugachev.popularmovies.movielist

import android.support.v4.content.Loader
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import ru.artempugachev.popularmovies.R
import ru.artempugachev.popularmovies.movielist.api.Movie

class MovieListPresenterImpl(private val model: MovieListMvpContract.Model) : MovieListMvpContract.Presenter {
    private var subscription: Disposable? = null
    private var view: MovieListMvpContract.View? = null
    private var currentPage = 1
    private var sortOrder = DEFAULT_SORT_ORDER


    /**
     * Load movies from Tmdb
     * */
    override fun loadMovies(sort: String, page: Int) {
        subscription = model.getMovies(sort, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Movie>() {
                    override fun onNext(movie: Movie) {
                        view?.updateMovies(movie)
                        view?.showProgress()
                    }


                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        view?.hideProgress()
                        view?.showErrorLoadingMovies()
                    }


                    override fun onComplete() {
                        view?.hideProgress()
                    }
                })
    }


    override fun loadMore(nextPage: Int) {
        var nextPage = nextPage
            // after rotation maybe problem with pagination.
            // if pages are the same, load next
            if (nextPage == currentPage) {
                nextPage++
            }

            currentPage = nextPage

        loadMovies(sortOrder, nextPage)
    }


    /**
     * Handle sort order changing
     * */
    override fun sortOrderChange(newSortOrder: String) {
        // clean list
        view?.emptyMovies()

        sortOrder = newSortOrder
        loadMovies(sortOrder, DEFAULT_PAGE_NUMBER)
    }


    override fun bindView(view: MovieListMvpContract.View) {
        this.view = view
    }


    override fun unsubscribeRx() {
        if (subscription?.isDisposed == false) {
            subscription?.dispose()
        }
    }


    companion object {
        const val SORT_ORDER_POPULAR = "popular"
        const val SORT_ORDER_TOP_RATED = "top_rated"
        const val DEFAULT_SORT_ORDER = SORT_ORDER_POPULAR
        const val DEFAULT_PAGE_NUMBER = 1
    }
}