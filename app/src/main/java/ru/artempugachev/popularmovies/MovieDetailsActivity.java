package ru.artempugachev.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.artempugachev.popularmovies.data.Movie;
import ru.artempugachev.popularmovies.data.MovieContract;
import ru.artempugachev.popularmovies.data.MoviesProvider;
import ru.artempugachev.popularmovies.data.Review;
import ru.artempugachev.popularmovies.data.Video;
import ru.artempugachev.popularmovies.ui.ReviewsAdapter;
import ru.artempugachev.popularmovies.ui.ReviewsLoader;
import ru.artempugachev.popularmovies.ui.TrailerLoader;
import ru.artempugachev.popularmovies.ui.TrailersAdapter;

public class MovieDetailsActivity extends AppCompatActivity implements TrailersAdapter.TrailerClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {
    private TextView titleTextView;
    private TextView yearTextView;
    private TextView overviewTextView;
    private TextView ratingTextView;
    private ImageView posterImageView;
    private ImageView backdropImageView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView mReviewsRecyclerView;
    private ReviewsAdapter mReviewsAdapter;
    private TextView mNoReviewsTextView;
    private LinearLayoutManager mReviewsLayoutManager;
    private RecyclerView mTrailersRecyclerView;
    private LinearLayoutManager mTrailersLayoutManager;
    private TrailersAdapter mTrailersAdapter;

    private final static int REVIEWS_LOADER_ID = 4242;
    private final static int TRAILERS_LOADER_ID = 777;
    private final static int IS_FAVORITE_LOADER_ID = 999;
    private final static String PAGE_NUMBER_KEY = "page_number";
    private final static String MOVIE_ID_KEY = "movie_id";
    private LoaderManager.LoaderCallbacks<List<Review>> reviewLoader;
    private LoaderManager.LoaderCallbacks<List<Video>> trailerLoader;
    private Movie mMovie;
    private Button mAddToFavButton;
    private Boolean mIsInFavorites;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpViews();

        Intent intent = getIntent();
        if (intent != null) {
            mMovie = intent.getParcelableExtra(MainActivity.MOVIE_EXTRA);
        }

        setData(mMovie);

        initReviewLoader(mMovie);
        initTrailerLoader(mMovie);

    }

    private void setUpViews() {
        titleTextView = (TextView) findViewById(R.id.details_title);
        yearTextView = (TextView) findViewById(R.id.details_year);
        overviewTextView = (TextView) findViewById(R.id.details_overview);
        ratingTextView = (TextView) findViewById(R.id.details_rating);
        posterImageView = (ImageView) findViewById(R.id.details_poster);
        backdropImageView = (ImageView) findViewById(R.id.movie_backdrop);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);

        mReviewsRecyclerView = (RecyclerView) findViewById(R.id.reviews_recycler);
        mReviewsAdapter = new ReviewsAdapter();
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);
        mReviewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewsRecyclerView.setLayoutManager(mReviewsLayoutManager);
        mNoReviewsTextView = (TextView) findViewById(R.id.no_reviews_text_view);

        mTrailersRecyclerView = (RecyclerView) findViewById(R.id.trailers_recycler_view);
        mTrailersAdapter = new TrailersAdapter(this, this);
        mTrailersRecyclerView.setAdapter(mTrailersAdapter);
        mTrailersLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTrailersRecyclerView.setLayoutManager(mTrailersLayoutManager);

        mAddToFavButton = (Button) findViewById(R.id.add_to_favorites);
        mAddToFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri moviesUri = MoviesProvider.Movies.MOVIES;
                getContentResolver().insert(moviesUri, mMovie.toContentValues());
            }
        });
    }

    /**
     * Set data from parcel extra to views
     * */
    private void setData(Movie movie) {
        titleTextView.setText(movie.getTitle());
        yearTextView.setText(movie.getReleaseDate());
        overviewTextView.setText(movie.getOverview());
        ratingTextView.setText(movie.getRating());

        Picasso.with(this).load(movie.getFullPosterPath()).into(posterImageView);
        Picasso.with(this).load(movie.getFullBackdropPath()).into(backdropImageView);
    }

    private void initReviewLoader(Movie movie) {
        reviewLoader = new LoaderManager.LoaderCallbacks<List<Review>>() {
            @Override
            public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
                switch (id) {
                    case REVIEWS_LOADER_ID:
                        int pageNumber = 1;
                        String movieId = null;

                        if (args != null) {
                            if (args.containsKey(PAGE_NUMBER_KEY)) {
                                pageNumber = args.getInt(PAGE_NUMBER_KEY, 1);
                            }

                            if (args.containsKey(MOVIE_ID_KEY)) {
                                movieId = args.getString(MOVIE_ID_KEY);
                            }
                        }
                        return new ReviewsLoader(MovieDetailsActivity.this, pageNumber, movieId);
                    default:
                        throw new RuntimeException("Loader not implemented: " + id);
                }
            }

            @Override
            public void onLoadFinished(Loader<List<Review>> loader, List<Review> reviews) {
                if (reviews != null) {
                    if (!reviews.isEmpty()) {
                        if (mReviewsAdapter.getItemCount() != 0) {
                            mReviewsAdapter.addData(reviews);
                        } else {
                            mReviewsAdapter.setData(reviews);
                        }
                        showReviewsRecyclerView();
                    } else {
                        showNoReviewsText();
                    }
                }
                else {
                    Toast.makeText(MovieDetailsActivity.this, R.string.no_reviews_data_message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onLoaderReset(Loader<List<Review>> loader) {

            }
        };

        Bundle reviewsLoaderBundle = new Bundle();
        reviewsLoaderBundle.putInt(PAGE_NUMBER_KEY, 1);
        if (movie != null) {
            reviewsLoaderBundle.putString(MOVIE_ID_KEY, movie.getId());
        }

        getSupportLoaderManager().initLoader(REVIEWS_LOADER_ID, reviewsLoaderBundle, reviewLoader);
    }

    private void initTrailerLoader(Movie movie) {
        trailerLoader = new LoaderManager.LoaderCallbacks<List<Video>>() {
            @Override
            public Loader<List<Video>> onCreateLoader(int id, Bundle args) {
                switch (id) {
                    case TRAILERS_LOADER_ID:
                        String movieId = null;

                        if (args != null) {
                            if (args.containsKey(MOVIE_ID_KEY)) {
                                movieId = args.getString(MOVIE_ID_KEY);
                            }
                        }

                        return new TrailerLoader(MovieDetailsActivity.this, movieId);
                    default:
                        throw new RuntimeException("Loader not implemented: " + id);
                }
            }

            @Override
            public void onLoadFinished(Loader<List<Video>> loader, List<Video> trailers) {
                if (trailers != null) {
                    if (!trailers.isEmpty()) {
                        if (mTrailersAdapter.getItemCount() != 0) {
                            mTrailersAdapter.addData(trailers);
                        } else {
                            mTrailersAdapter.setData(trailers);
                        }
                    }
                }
                else {
                    Toast.makeText(MovieDetailsActivity.this, R.string.no_trailers_data_message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onLoaderReset(Loader<List<Video>> loader) {

            }
        };

        Bundle trailerLoaderBundle = new Bundle();
        trailerLoaderBundle.putString(MOVIE_ID_KEY, movie.getId());

        getSupportLoaderManager().initLoader(TRAILERS_LOADER_ID, trailerLoaderBundle, trailerLoader);
    }

    private void showNoReviewsText() {
        mNoReviewsTextView.setVisibility(View.VISIBLE);
        mReviewsRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showReviewsRecyclerView() {
        mNoReviewsTextView.setVisibility(View.INVISIBLE);
        mReviewsRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTrailerClick(String urlStr) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlStr)));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        /**
         * This loader checks if current movie is in favorites
         * Use it to set state of add to favorites button
         * Try to load movie from db. If result is not empty, the movie is in favorites
         * Otherwise it isn't.
         * Set caption on add to favorites button respectively.
         * */
        switch (loaderId) {
            case IS_FAVORITE_LOADER_ID:
                Uri uri = MoviesProvider.Movies.withId(mMovie.getId());
                return new CursorLoader(this, uri, null, null, null, null);

            default:
                throw new RuntimeException("Loader not implemented: " + loaderId);
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor movie) {
        if (movie != null && movie.getCount() > 0) {
            // Movie in favorites
            mIsInFavorites = true;
            mAddToFavButton.setText(R.string.remove_from_favorites);
        } else {
            mIsInFavorites = false;
            mAddToFavButton.setText(R.string.add_to_favorites);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
