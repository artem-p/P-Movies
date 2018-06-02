package ru.artempugachev.popularmovies.moviedetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.content_movie_details.*
import ru.artempugachev.popularmovies.MoviesApplication

import ru.artempugachev.popularmovies.R
import ru.artempugachev.popularmovies.data.MovieContract
import ru.artempugachev.popularmovies.movielist.api.Movie
import ru.artempugachev.popularmovies.moviedetails.api.Review
import ru.artempugachev.popularmovies.moviedetails.api.Video
import ru.artempugachev.popularmovies.movielist.MovieListActivity
import javax.inject.Inject


class MovieDetailsActivity : AppCompatActivity(), TrailersAdapter.TrailerClickListener, MovieDetailsMvpContract.View {
    private var reviewsAdapter: ReviewsAdapter? = null
    private var reviewsLayoutManager: LinearLayoutManager? = null
    private var trailersLayoutManager: LinearLayoutManager? = null
    private var trailersAdapter: TrailersAdapter? = null
    private var reviewLoader: LoaderManager.LoaderCallbacks<List<Review>>? = null
    private var movie: Movie? = null
    private var isMovieInFavorites: Boolean? = null

    @Inject
    lateinit var presenter: MovieDetailsMvpContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        (application as MoviesApplication).getMovieDetailsComponent().inject(this)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        setUpViews()

        val intent = intent
        if (intent != null && intent.hasExtra(MovieListActivity.MOVIE_EXTRA)) {
            movie = intent.getParcelableExtra(MovieListActivity.MOVIE_EXTRA)
        }

        movie?.let { setData(movie as Movie) }
    }


    private fun setUpViews() {
        reviewsAdapter = ReviewsAdapter()
        reviewsRecycler.adapter = reviewsAdapter
        reviewsLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        reviewsRecycler.layoutManager = reviewsLayoutManager

        trailersAdapter = TrailersAdapter(this, this)
        trailersRecycler.adapter = trailersAdapter
        trailersLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        trailersRecycler.layoutManager = trailersLayoutManager

        addToFaforites.setOnClickListener {
            // add to favorites or remove is movie already favorite
            if ((isMovieInFavorites == false )) {
                addToFavorites()
            } else {
                removeFromFavorites()
            }
        }
    }


    /**
     * View methods
     * */
    override fun showDetails(movie: Movie) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showTrailers(trailers: List<Video>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showReviews(reviews: List<Review>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showTrailersError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showReviewsError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun addToFavorites() {
        val moviesUri = MovieContract.MOVIES_URI
        contentResolver.insert(moviesUri, movie!!.toContentValues())
    }


    private fun removeFromFavorites() {
        contentResolver.delete(MovieContract.uriWithId(movie!!.id), null, null)
    }

    /**
     * Set data from parcel extra to views
     */
    private fun setData(movie: Movie) {
        detailsTitle.text = movie.title
        detailsYear.text = movie.releaseDate
        detailsOverview.text = movie.overview
        detailsRating.text = movie.rating

        Picasso.with(this).load(movie.fullPosterPath).into(detailsPoster)
        Picasso.with(this).load(movie.fullBackdropPath).into(detailsBackdrop)
    }


    private fun initReviewLoader(movie: Movie?) {
        reviewLoader = object : LoaderManager.LoaderCallbacks<List<Review>> {
            override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<Review>> {
                when (id) {
                    REVIEWS_LOADER_ID -> {
                        var pageNumber = 1
                        var movieId: String? = null

                        if (args != null) {
                            if (args.containsKey(PAGE_NUMBER_KEY)) {
                                pageNumber = args.getInt(PAGE_NUMBER_KEY, 1)
                            }

                            if (args.containsKey(MOVIE_ID_KEY)) {
                                movieId = args.getString(MOVIE_ID_KEY)
                            }
                        }
                        return ReviewsLoader(this@MovieDetailsActivity, pageNumber, movieId)
                    }
                    else -> throw RuntimeException("Loader not implemented: $id")
                }
            }

            override fun onLoadFinished(loader: Loader<List<Review>>, reviews: List<Review>?) {
                if (reviews != null) {
                    if (!reviews.isEmpty()) {
                        if (reviewsAdapter!!.itemCount != 0) {
                            reviewsAdapter!!.addData(reviews)
                        } else {
                            reviewsAdapter!!.setData(reviews)
                        }
                        showReviews()
                    } else {
                        showNoReviewsText()
                    }
                } else {
                    Toast.makeText(this@MovieDetailsActivity, R.string.no_reviews_data_message, Toast.LENGTH_SHORT).show()
                }

            }

            override fun onLoaderReset(loader: Loader<List<Review>>) {

            }
        }

        val reviewsLoaderBundle = Bundle()
        reviewsLoaderBundle.putInt(PAGE_NUMBER_KEY, 1)
        if (movie != null) {
            reviewsLoaderBundle.putString(MOVIE_ID_KEY, movie.id)
        }

        supportLoaderManager.initLoader(REVIEWS_LOADER_ID, reviewsLoaderBundle, reviewLoader!!)
    }


    private fun loadTrailers(movie: Movie) {
        //        TmdbApiClient tmdbApiClient = new TmdbApiClient();
        //        TmdbApiInterface tmdbApiInterface = tmdbApiClient.buildApiInterface();
        //
        //        if (movie.getId() != null) {
        //            Observable<VideoResponse> trailers = tmdbApiInterface.getVideos(movie.getId(), BuildConfig.TMDB_API_KEY);
        //
        //            trailers.subscribeOn(Schedulers.newThread())
        //                    .observeOn(AndroidSchedulers.mainThread())
        //                    .subscribe(trailerResponse -> {
        //                        Log.e("Trailers", trailerResponse.getResults().get(0).getName());
        //                    });
        //        }

        //        trailerLoader = new LoaderManager.LoaderCallbacks<List<Video>>() {
        //            @Override
        //            public Loader<List<Video>> onCreateLoader(int id, Bundle args) {
        //                switch (id) {
        //                    case TRAILERS_LOADER_ID:
        //                        String movieId = null;
        //
        //                        if (args != null) {
        //                            if (args.containsKey(MOVIE_ID_KEY)) {
        //                                movieId = args.getString(MOVIE_ID_KEY);
        //                            }
        //                        }
        //
        //                        return new TrailerLoader(MovieDetailsActivity.this, movieId);
        //                    default:
        //                        throw new RuntimeException("Loader not implemented: " + id);
        //                }
        //            }
        //
        //            @Override
        //            public void onLoadFinished(Loader<List<Video>> loader, List<Video> trailers) {
        //                if (trailers != null) {
        //                    if (!trailers.isEmpty()) {
        //                        if (trailersAdapter.getItemCount() != 0) {
        //                            trailersAdapter.addData(trailers);
        //                        } else {
        //                            trailersAdapter.setData(trailers);
        //                        }
        //                    }
        //                }
        //                else {
        //                    Toast.makeText(MovieDetailsActivity.this, R.string.no_trailers_data_message, Toast.LENGTH_SHORT).show();
        //                }
        //
        //            }
        //
        //            @Override
        //            public void onLoaderReset(Loader<List<Video>> loader) {
        //
        //            }
        //        };
        //
        //        Bundle trailerLoaderBundle = new Bundle();
        //        trailerLoaderBundle.putString(MOVIE_ID_KEY, movie.getId());
        //
        //        getSupportLoaderManager().initLoader(TRAILERS_LOADER_ID, trailerLoaderBundle, trailerLoader);
    }

    private fun showNoReviewsText() {
        noReviewsTextView.visibility = View.VISIBLE
        reviewsRecycler.visibility = View.INVISIBLE
    }


    private fun showReviews() {
        noReviewsTextView.visibility = View.INVISIBLE
        reviewsRecycler.visibility = View.VISIBLE
    }


    override fun onTrailerClick(urlStr: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlStr)))
    }


//    override fun onCreateLoader(loaderId: Int, args: Bundle?): Loader<Cursor> {
//        /**
//         * This loader checks if current movie is in favorites
//         * Use it to set state of add to favorites button
//         * Try to load movie from db. If result is not empty, the movie is in favorites
//         * Otherwise it isn't.
//         * Set caption on add to favorites button respectively.
//         */
//        when (loaderId) {
//            IS_FAVORITE_LOADER_ID -> {
//                val uri = MovieContract.uriWithId(movie!!.id)
//                return CursorLoader(this, uri, null, null, null, null)
//            }
//
//            else -> throw RuntimeException("Loader not implemented: $loaderId")
//        }
//
//    }

//    override fun onLoadFinished(loader: Loader<Cursor>, movie: Cursor?) {
//        if (movie != null && movie.count > 0) {
//            // Movie in favorites
//            isMovieInFavorites = true
//            mAddToFavButton!!.setText(R.string.remove_from_favorites)
//        } else {
//            isMovieInFavorites = false
//            mAddToFavButton!!.setText(R.string.add_to_favorites)
//        }
//    }

//    override fun onLoaderReset(loader: Loader<Cursor>) {
//
//    }

    companion object {

        private val REVIEWS_LOADER_ID = 4242
        private val TRAILERS_LOADER_ID = 777
        private val IS_FAVORITE_LOADER_ID = 999
        private val PAGE_NUMBER_KEY = "page_number"
        private val MOVIE_ID_KEY = "movie_id"
    }
}
