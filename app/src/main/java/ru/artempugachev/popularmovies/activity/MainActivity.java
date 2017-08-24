package ru.artempugachev.popularmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.artempugachev.popularmovies.loader.MoviesGridLoader;
import ru.artempugachev.popularmovies.R;
import ru.artempugachev.popularmovies.model.Movie;
import ru.artempugachev.popularmovies.ui.EndlessRecyclerViewScrollListener;
import ru.artempugachev.popularmovies.ui.MoviesGridAdapter;
import ru.artempugachev.popularmovies.ui.SortOrderDialog;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>,
        MoviesGridAdapter.MoviesGridClickListener, SortOrderDialog.SortOrderDialogListener,
        MoviesGridLoader.MoviesLoadListener {

    private final static int MOVIES_GRID_LOADER_ID = 42;
    public static final String MOVIE_EXTRA = "movie_extra";
    private static final String SORT_ORDER_DIALOG_TAG = "sort_order_dialog";
    private static final String PAGE_NUMBER_KEY = "page_number";
    private static final String SORT_ORDER_KEY = "sorting";
    public static final String DEFAULT_SORT_ORDER_ID = "popular";
    private String sortOrderId = DEFAULT_SORT_ORDER_ID;

    private MoviesGridAdapter moviesGridAdapter;
    private ProgressBar progressBar;
    private TextView noFavoritesTextView;
    private EndlessRecyclerViewScrollListener scrollListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpViews();

        getSupportLoaderManager().initLoader(MOVIES_GRID_LOADER_ID, null, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    private void setUpViews() {
        RecyclerView mMovieGridRecyclerView = (RecyclerView) findViewById(R.id.rv_movies_grid);

        GridLayoutManager moviesLayoutManager = new GridLayoutManager(this, getNumberOfColumnsInGrid());
        mMovieGridRecyclerView.setLayoutManager(moviesLayoutManager);

        mMovieGridRecyclerView.setHasFixedSize(true);

        scrollListener = new EndlessRecyclerViewScrollListener(moviesLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (!sortOrderId.equals(getString(R.string.sort_order_id_favorites))) {
                    Bundle loaderBundle = new Bundle();
                    loaderBundle.putInt(PAGE_NUMBER_KEY, page);
                    loaderBundle.putString(SORT_ORDER_KEY, sortOrderId);
                    getSupportLoaderManager().restartLoader(MOVIES_GRID_LOADER_ID, loaderBundle, MainActivity.this);
                } else {
                    // do nothing, don't use endless scroll in favorites
                }
            }
        };

        mMovieGridRecyclerView.addOnScrollListener(scrollListener);

        moviesGridAdapter = new MoviesGridAdapter(this, this);
        mMovieGridRecyclerView.setAdapter(moviesGridAdapter);

        progressBar = (ProgressBar) findViewById(R.id.moviesGridProgressBar);
        noFavoritesTextView = (TextView) findViewById(R.id.no_favorites_text_view);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_sort:
                SortOrderDialog sortOrderDialog = new SortOrderDialog();
                sortOrderDialog.show(getFragmentManager(), SORT_ORDER_DIALOG_TAG);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        noFavoritesTextView.setVisibility(View.INVISIBLE);
        switch (id) {
            case MOVIES_GRID_LOADER_ID:
                int pageNumber = 1;
                String sortOrder = getResources().getString(R.string.sort_order_id_popular);
                if (args != null && args.containsKey(PAGE_NUMBER_KEY)) {
                    pageNumber = args.getInt(PAGE_NUMBER_KEY, 1);
                }

                if (args != null && args.containsKey(SORT_ORDER_KEY)) {
                    sortOrder = args.getString(SORT_ORDER_KEY, getResources().getString(R.string.sort_order_id_popular));
                }

                return new MoviesGridLoader(this, this, pageNumber, sortOrder);
            default:
                throw new RuntimeException("Loader not implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {


        if (movies != null && !movies.isEmpty()) {
            if (moviesGridAdapter.getItemCount() != 0) {
                moviesGridAdapter.addData(movies);
            } else {
                moviesGridAdapter.setData(movies);
            }
        } else {
            if (!this.sortOrderId.equals(getString(R.string.sort_order_id_favorites))) {
                // popular, top rated. Just toast
                Toast.makeText(this, R.string.no_movies_data_message, Toast.LENGTH_SHORT).show();
            } else {
                // favorites. Show message about no favorites
                noFavoritesTextView.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        noFavoritesTextView.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onMovieClick(int position, View v) {
        Movie movie = moviesGridAdapter.getMovie(position);
        if (movie != null) {
            Intent movieDetailsActivityIntent = new Intent(MainActivity.this, MovieDetailsActivity.class);
            movieDetailsActivityIntent.putExtra(MOVIE_EXTRA, movie);

            View ivPosterInGrid =  v.findViewById(R.id.posterImage);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    ivPosterInGrid, getString(R.string.poster_transition));
            startActivity(movieDetailsActivityIntent, options.toBundle());
        }
    }


    @Override
    public void onSortOrderChange(int posInDialog) {
        moviesGridAdapter.setData(null);
        moviesGridAdapter.notifyDataSetChanged();
        scrollListener.resetState();

        this.sortOrderId = getResources().getStringArray(R.array.sort_orders_id)[posInDialog];

        Loader loader = getSupportLoaderManager().getLoader(MOVIES_GRID_LOADER_ID);
        MoviesGridLoader moviesGridLoader = (MoviesGridLoader) loader;
        moviesGridLoader.changeSortOrder(this.sortOrderId);
    }

    @Override
    public void onStartLoadingMovies() {
        noFavoritesTextView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFinishLoadingMovies() {
        progressBar.setVisibility(View.INVISIBLE);
    }


    /**
     * Calculate number of columns in grid based on device width
     * */
    private int getNumberOfColumnsInGrid() {

        final int SCALING_FACTOR = 180;

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        int numberOfColumns = (int) (dpWidth / SCALING_FACTOR);

        return numberOfColumns;
    }
}
