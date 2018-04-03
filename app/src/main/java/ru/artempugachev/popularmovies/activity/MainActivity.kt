package ru.artempugachev.popularmovies.activity

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso

import ru.artempugachev.popularmovies.loader.MoviesGridLoader
import ru.artempugachev.popularmovies.R
import ru.artempugachev.popularmovies.di.ContextModule
import ru.artempugachev.popularmovies.di.DaggerMovieComponent
import ru.artempugachev.popularmovies.di.MovieComponent
import ru.artempugachev.popularmovies.model.Movie
import ru.artempugachev.popularmovies.ui.EndlessRecyclerViewScrollListener
import ru.artempugachev.popularmovies.ui.MoviesGridAdapter
import ru.artempugachev.popularmovies.ui.SortOrderDialog

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<List<Movie>>, MoviesGridAdapter.MoviesGridClickListener, SortOrderDialog.SortOrderDialogListener, MoviesGridLoader.MoviesLoadListener {
    private var sortOrderId = DEFAULT_SORT_ORDER_ID
    private var currentPage = DEFAULT_PAGE_NUMBER

    private var moviesGridAdapter: MoviesGridAdapter? = null
    private var progressBar: ProgressBar? = null
    private var noFavoritesTextView: TextView? = null
    private var scrollListener: EndlessRecyclerViewScrollListener? = null

    private lateinit var picasso: Picasso

    /**
     * Calculate number of columns in grid based on device width
     */
    private val numberOfColumnsInGrid: Int
        get() {

            val SCALING_FACTOR = 180

            val displayMetrics = resources.displayMetrics
            val dpWidth = displayMetrics.widthPixels / displayMetrics.density

            val numberOfColumns = (dpWidth / SCALING_FACTOR).toInt()

            return (dpWidth / SCALING_FACTOR).toInt()
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)


        val movieComponent = DaggerMovieComponent.builder()
                .contextModule(ContextModule(this))
                .build()

        picasso = movieComponent.getPicasso()

        setUpViews()

        val loaderBundle = Bundle()

        moviesGridAdapter!!.setData(ArrayList())

        if (savedInstanceState != null) {
            currentPage = savedInstanceState.getInt(PAGE_NUMBER_KEY, DEFAULT_PAGE_NUMBER)
            sortOrderId = savedInstanceState.getString(SORT_ORDER_KEY, DEFAULT_SORT_ORDER_ID)

            loaderBundle.putInt(PAGE_NUMBER_KEY, currentPage)
            loaderBundle.putString(SORT_ORDER_KEY, sortOrderId)

            val savedMovies = savedInstanceState.getParcelableArrayList<Movie>(MOVIES_LIST_KEY)

            moviesGridAdapter!!.setData(savedMovies)
            scrollListener!!.resetState()
        }

        supportLoaderManager.initLoader(MOVIES_GRID_LOADER_ID, loaderBundle, this)
    }


    public override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putString(SORT_ORDER_KEY, sortOrderId)
        outState.putInt(PAGE_NUMBER_KEY, currentPage)
        outState.putParcelableArrayList(MOVIES_LIST_KEY, moviesGridAdapter!!.getMovies())
    }


    override fun onResume() {
        super.onResume()
        // if sort order is favorite, restart loader to maintain possible changes in favorites
        if (sortOrderId == getString(R.string.sort_order_id_favorites)) {
            moviesGridAdapter!!.setData(ArrayList())
            val loaderBundle = Bundle()
            loaderBundle.putString(SORT_ORDER_KEY, sortOrderId)
            supportLoaderManager.restartLoader(MOVIES_GRID_LOADER_ID, loaderBundle, this)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    private fun setUpViews() {
        val mMovieGridRecyclerView = findViewById<View>(R.id.rv_movies_grid) as RecyclerView

        val moviesLayoutManager = GridLayoutManager(this, numberOfColumnsInGrid)
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
                    supportLoaderManager.restartLoader(MOVIES_GRID_LOADER_ID, loaderBundle, this@MainActivity)
                } else {
                    // do nothing, don't use endless scroll in favorites
                }
            }
        }

        mMovieGridRecyclerView.addOnScrollListener(scrollListener)

        moviesGridAdapter = MoviesGridAdapter(this, this, picasso)
        mMovieGridRecyclerView.adapter = moviesGridAdapter

        progressBar = findViewById<View>(R.id.moviesGridProgressBar) as ProgressBar
        noFavoritesTextView = findViewById<View>(R.id.no_favorites_text_view) as TextView
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

                return MoviesGridLoader(this, this, pageNumber, sortOrder)
            }
            else -> throw RuntimeException("Loader not implemented: $id")
        }
    }

    override fun onLoadFinished(loader: Loader<List<Movie>>, movies: List<Movie>?) {


        if (movies != null && !movies.isEmpty()) {
            if (moviesGridAdapter!!.itemCount != 0) {
                moviesGridAdapter!!.addData(movies)
            } else {
                moviesGridAdapter!!.setData(movies)
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
        val movie = moviesGridAdapter!!.getMovie(position)
        if (movie != null) {
            val movieDetailsActivityIntent = Intent(this@MainActivity, MovieDetailsActivity::class.java)
            movieDetailsActivityIntent.putExtra(MOVIE_EXTRA, movie)

            val ivPosterInGrid = v.findViewById<View>(R.id.posterImage)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    ivPosterInGrid, getString(R.string.poster_transition))
            startActivity(movieDetailsActivityIntent, options.toBundle())
        }
    }


    override fun onSortOrderChange(posInDialog: Int) {
        moviesGridAdapter!!.setData(ArrayList())
        moviesGridAdapter!!.notifyDataSetChanged()
        scrollListener!!.resetState()

        this.sortOrderId = resources.getStringArray(R.array.sort_orders_id)[posInDialog]

        val loader: Loader<Any>? = supportLoaderManager.getLoader(MOVIES_GRID_LOADER_ID)
        val moviesGridLoader = loader as MoviesGridLoader
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
        val DEFAULT_SORT_ORDER_ID = "popular"
        val DEFAULT_PAGE_NUMBER = 1
    }
}
