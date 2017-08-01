package ru.artempugachev.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.artempugachev.popularmovies.data.Movie;
import ru.artempugachev.popularmovies.data.Review;
import ru.artempugachev.popularmovies.ui.ReviewsAdapter;
import ru.artempugachev.popularmovies.ui.ReviewsLoader;

public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Review>> {
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
    private LinearLayoutManager mLayoutManager;

    private final static int REVIEWS_LOADER_ID = 4242;
    private final static String PAGE_NUMBER_KEY = "page_number";
    private final static String MOVIE_ID_KEY = "movie_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpViews();

        Movie movie = null;
        Intent intent = getIntent();
        if (intent != null) {
            movie = intent.getParcelableExtra(MainActivity.MOVIE_EXTRA);
        }

        setData(movie);

        Bundle reviewsLoaderBundle = new Bundle();
        reviewsLoaderBundle.putInt(PAGE_NUMBER_KEY, 1);
        if (movie != null) {
           reviewsLoaderBundle.putString(MOVIE_ID_KEY, movie.getId());
        }

        getSupportLoaderManager().initLoader(REVIEWS_LOADER_ID, reviewsLoaderBundle, this);

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
        mReviewsAdapter = new ReviewsAdapter(this);
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewsRecyclerView.setLayoutManager(mLayoutManager);
        mNoReviewsTextView = (TextView) findViewById(R.id.no_reviews_text_view);
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


    /**
     * Loader methods
     * */
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
                return new ReviewsLoader(this, pageNumber, movieId);
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
            Toast.makeText(this, R.string.no_reviews_data_message, Toast.LENGTH_SHORT).show();
        }

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
    public void onLoaderReset(Loader<List<Review>> loader) {

    }
}
