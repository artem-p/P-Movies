package ru.artempugachev.popularmovies.movielist

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
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
import kotlinx.android.synthetic.main.content_main.*
import ru.artempugachev.popularmovies.MoviesApplication

import ru.artempugachev.popularmovies.R
import ru.artempugachev.popularmovies.moviedetails.MovieDetailsActivity
import ru.artempugachev.popularmovies.movielist.MovieListPresenterImpl.Companion.DEFAULT_SORT_ORDER
import ru.artempugachev.popularmovies.movielist.api.Movie
import javax.inject.Inject

class MovieListActivity : AppCompatActivity(),
        MovieListAdapter.MoviesGridClickListener,
        SortOrderDialog.SortOrderDialogListener,
        MovieListMvpContract.View {

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
    }


    override fun onResume() {
        super.onResume()

        presenter.bindView(this)
        presenter.loadMovies(DEFAULT_SORT_ORDER, 1)
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribeRx()
    }


    // implement view methods
    override fun emptyMovies() {
        movieListAdapter?.empty()
        scrollListener?.resetState()
    }


    override fun updateMovies(movie: Movie) {
        movieListAdapter?.addMovie(movie)
    }


    /**
     * Show progress indicator while loading movies
     * */
    override fun showProgress() {
        if (moviesGridProgressBar.visibility == View.INVISIBLE)
            moviesGridProgressBar.visibility = View.VISIBLE
    }


    /**
     * Hide progress indicator when movies are loaded
     * */
    override fun hideProgress() {
        moviesGridProgressBar.visibility = View.INVISIBLE
    }


    override fun showMovieDetail(movie: Movie, adapterView: View) {
        val movieDetailsActivityIntent = Intent(this@MovieListActivity, MovieDetailsActivity::class.java)
        movieDetailsActivityIntent.putExtra(MOVIE_EXTRA, movie)

        val ivPosterInGrid = adapterView.findViewById<View>(R.id.posterImage)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                ivPosterInGrid, getString(R.string.poster_transition))
        startActivity(movieDetailsActivityIntent, options.toBundle())
    }


    override fun showErrorLoadingMovies() {
        showToast(getString(R.string.error_loading_movies))
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    public override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
//        outState!!.putString(SORT_ORDER_KEY, sortOrderId)
//        outState.putInt(PAGE_NUMBER_KEY, currentPage)
//        outState.putParcelableArrayList(MOVIES_LIST_KEY, movieListAdapter!!.getMovies())
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
                presenter.loadMore(nextPage)
            }
        }

        mMovieGridRecyclerView.addOnScrollListener(scrollListener)

        movieListAdapter = MovieListAdapter(this, this, picasso)
        mMovieGridRecyclerView.adapter = movieListAdapter

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


    override fun onMovieClick(movie: Movie, adapterView: View) {
        presenter.onMovieClick(movie, adapterView)
    }


    override fun onSortOrderChange(posInDialog: Int) {
        val sortOrder = resources.getStringArray(R.array.sort_orders_id)[posInDialog]

        presenter.sortOrderChange(sortOrder)
    }


    companion object {

        private val MOVIES_GRID_LOADER_ID = 42
        val MOVIE_EXTRA = "movie_extra"
        private val SORT_ORDER_DIALOG_TAG = "sort_order_dialog"
        private val PAGE_NUMBER_KEY = "page_number"
        private val SORT_ORDER_KEY = "sorting"
        private val MOVIES_LIST_KEY = "movies"
    }
}
