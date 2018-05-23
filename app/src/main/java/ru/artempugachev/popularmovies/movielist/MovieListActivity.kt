package ru.artempugachev.popularmovies.movielist

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import ru.artempugachev.popularmovies.MoviesApplication

import ru.artempugachev.popularmovies.R
import ru.artempugachev.popularmovies.moviedetails.MovieDetailsActivity
import ru.artempugachev.popularmovies.movielist.api.Movie
import javax.inject.Inject

class MovieListActivity : AppCompatActivity(),
        LoaderManager.LoaderCallbacks<List<Movie>>,
        MovieListAdapter.MoviesGridClickListener,
        SortOrderDialog.SortOrderDialogListener,
        MovieListLoader.MoviesLoadListener,
        MovieListMvpContract.View {



    private var sortOrderId = DEFAULT_SORT_ORDER
    private var currentPage = DEFAULT_PAGE_NUMBER

    private var movieListAdapter: MovieListAdapter? = null
    private var progressBar: ProgressBar? = null
    private var noFavoritesTextView: TextView? = null
    private var scrollListener: EndlessRecyclerViewScrollListener? = null

    private lateinit var picasso: Picasso

    @Inject
    lateinit var presenter: MovieListMvpContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpComponents()

        setUpViews()

//        setUpLoader(savedInstanceState)
    }


    override fun onResume() {
        super.onResume()

        presenter.bindView(this)
        presenter.loadMovies(DEFAULT_SORT_ORDER, 1)

        // if sort order is favorite, restart loader to maintain possible changes in favorites
        if (sortOrderId == getString(R.string.sort_order_id_favorites)) {
            movieListAdapter!!.setData(ArrayList())
            val loaderBundle = Bundle()
            loaderBundle.putString(SORT_ORDER_KEY, sortOrderId)
            supportLoaderManager.restartLoader(MOVIES_GRID_LOADER_ID, loaderBundle, this)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribeRx()
    }



    // implement view methods
    override fun updateMovies(movies: List<Movie>) {
        movieListAdapter?.setData(movies)
    }


    override fun showMovies() {
    }


    override fun showProgress() {
    }


    override fun showMovieDetail() {

    }


    override fun showErrorLoadingMovies() {
        showToast(getString(R.string.error_loading_movies))
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    public override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putString(SORT_ORDER_KEY, sortOrderId)
        outState.putInt(PAGE_NUMBER_KEY, currentPage)
        outState.putParcelableArrayList(MOVIES_LIST_KEY, movieListAdapter!!.getMovies())
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    private fun setUpViews() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        setUpRecycler()

        progressBar = findViewById<View>(R.id.moviesGridProgressBar) as ProgressBar
        noFavoritesTextView = findViewById<View>(R.id.no_favorites_text_view) as TextView
    }


    private fun setUpComponents() {
        picasso = (application as MoviesApplication).getMovieComponent().getPicasso()

        (application as MoviesApplication).getMovieListComponent().inject(this)
    }


    private fun setUpRecycler() {
        val mMovieGridRecyclerView = findViewById<View>(R.id.rv_movies_grid) as RecyclerView

        val moviesLayoutManager = GridLayoutManager(this, getNumberOfColumnsInGrid())
        mMovieGridRecyclerView.layoutManager = moviesLayoutManager

        mMovieGridRecyclerView.setHasFixedSize(true)

        scrollListener = object : EndlessRecyclerViewScrollListener(moviesLayoutManager) {
            override fun onLoadMore(nextPage: Int, totalItemsCount: Int, view: RecyclerView) {
                var nextPage = nextPage
                if (sortOrderId != getString(R.string.sort_order_id_favorites)) {

                    // after rotation maybe problem with pagination.
                    // if pages are the same, load next
                    if (nextPage == currentPage) {
                        nextPage++
                    }

                    currentPage = nextPage
                    val loaderBundle = Bundle()
                    loaderBundle.putInt(PAGE_NUMBER_KEY, nextPage)
                    loaderBundle.putString(SORT_ORDER_KEY, sortOrderId)
                    supportLoaderManager.restartLoader(MOVIES_GRID_LOADER_ID, loaderBundle, this@MovieListActivity)
                } else {
                    // do nothing, don't use endless scroll in favorites
                }
            }
        }

        mMovieGridRecyclerView.addOnScrollListener(scrollListener)

        movieListAdapter = MovieListAdapter(this, this, picasso)
        mMovieGridRecyclerView.adapter = movieListAdapter

    }


    private fun setUpLoader(savedInstanceState: Bundle?) {
        val loaderBundle = Bundle()

        movieListAdapter!!.setData(ArrayList())

        if (savedInstanceState != null) {
            currentPage = savedInstanceState.getInt(PAGE_NUMBER_KEY, DEFAULT_PAGE_NUMBER)
            sortOrderId = savedInstanceState.getString(SORT_ORDER_KEY, DEFAULT_SORT_ORDER)

            loaderBundle.putInt(PAGE_NUMBER_KEY, currentPage)
            loaderBundle.putString(SORT_ORDER_KEY, sortOrderId)

            val savedMovies = savedInstanceState.getParcelableArrayList<Movie>(MOVIES_LIST_KEY)

            movieListAdapter!!.setData(savedMovies)
            scrollListener!!.resetState()
        }

        supportLoaderManager.initLoader(MOVIES_GRID_LOADER_ID, loaderBundle, this)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.action_sort -> {
                val sortOrderDialog = SortOrderDialog()
                sortOrderDialog.show(fragmentManager, SORT_ORDER_DIALOG_TAG)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * Calculate number of columns in grid based on device width
     */
    private fun getNumberOfColumnsInGrid(): Int {

        val SCALING_FACTOR = 180

        val displayMetrics = resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density

        val numberOfColumns = (dpWidth / SCALING_FACTOR).toInt()

        return (dpWidth / SCALING_FACTOR).toInt()
    }


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<Movie>> {
        noFavoritesTextView!!.visibility = View.INVISIBLE
        when (id) {
            MOVIES_GRID_LOADER_ID -> {
                var pageNumber = 1
                var sortOrder = resources.getString(R.string.sort_order_id_popular)
                if (args != null && args.containsKey(PAGE_NUMBER_KEY)) {
                    pageNumber = args.getInt(PAGE_NUMBER_KEY, DEFAULT_PAGE_NUMBER)
                }

                if (args != null && args.containsKey(SORT_ORDER_KEY)) {
                    sortOrder = args.getString(SORT_ORDER_KEY, resources.getString(R.string.sort_order_id_popular))
                }

                return MovieListLoader(this, this, pageNumber, sortOrder)
            }
            else -> throw RuntimeException("Loader not implemented: $id")
        }
    }

    override fun onLoadFinished(loader: Loader<List<Movie>>, movies: List<Movie>?) {


        if (movies != null && !movies.isEmpty()) {
            if (movieListAdapter!!.itemCount != 0) {
                movieListAdapter!!.addData(movies)
            } else {
                movieListAdapter!!.setData(movies)
            }
        } else {
            if (this.sortOrderId != getString(R.string.sort_order_id_favorites)) {
                // popular, top rated. Just toast
                Toast.makeText(this, R.string.no_movies_data_message, Toast.LENGTH_SHORT).show()
            } else {
                // favorites. Show message about no favorites
                noFavoritesTextView!!.visibility = View.VISIBLE
            }
        }
    }


    override fun onLoaderReset(loader: Loader<List<Movie>>) {
        noFavoritesTextView!!.visibility = View.INVISIBLE
    }


    override fun onMovieClick(position: Int, v: View) {
        val movie = movieListAdapter!!.getMovie(position)
        if (movie != null) {
            val movieDetailsActivityIntent = Intent(this@MovieListActivity, MovieDetailsActivity::class.java)
            movieDetailsActivityIntent.putExtra(MOVIE_EXTRA, movie)

            val ivPosterInGrid = v.findViewById<View>(R.id.posterImage)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    ivPosterInGrid, getString(R.string.poster_transition))
            startActivity(movieDetailsActivityIntent, options.toBundle())
        }
    }


    override fun onSortOrderChange(posInDialog: Int) {
        movieListAdapter!!.setData(ArrayList())
        movieListAdapter!!.notifyDataSetChanged()
        scrollListener!!.resetState()

        this.sortOrderId = resources.getStringArray(R.array.sort_orders_id)[posInDialog]

        val loader: Loader<Any>? = supportLoaderManager.getLoader(MOVIES_GRID_LOADER_ID)
        val moviesGridLoader = loader as MovieListLoader
        moviesGridLoader.changeSortOrder(this.sortOrderId)
    }

    override fun onStartLoadingMovies() {
        noFavoritesTextView!!.visibility = View.INVISIBLE
        progressBar!!.visibility = View.VISIBLE
    }

    override fun onFinishLoadingMovies() {
        progressBar!!.visibility = View.INVISIBLE
    }

    companion object {

        private val MOVIES_GRID_LOADER_ID = 42
        val MOVIE_EXTRA = "movie_extra"
        private val SORT_ORDER_DIALOG_TAG = "sort_order_dialog"
        private val PAGE_NUMBER_KEY = "page_number"
        private val SORT_ORDER_KEY = "sorting"
        private val MOVIES_LIST_KEY = "movies"
        val DEFAULT_SORT_ORDER = "popular"
        val DEFAULT_PAGE_NUMBER = 1
    }
}
