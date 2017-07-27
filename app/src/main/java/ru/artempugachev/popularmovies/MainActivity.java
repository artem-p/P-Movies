package ru.artempugachev.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import ru.artempugachev.popularmovies.data.Movie;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>,
        MoviesGridAdapter.MoviesGridClickListener, SortOrderDialog.SortOrderDialogListener,
        MoviesGridLoader.MoviesLoadListener {

    private final static int MOVIES_GRID_LOADER_ID = 42;
    public static final String MOVIE_EXTRA = "movie_extra";
    private static final String SORT_ORDER_DIALOG_TAG = "sort_order_dialog";

    private MoviesGridAdapter moviesGridAdapter;
    private ProgressBar progressBar;

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
        RecyclerView mMoviesGridRecyclerView = (RecyclerView) findViewById(R.id.rv_movies_grid);

        GridLayoutManager moviesLayoutManager = new GridLayoutManager(this, getNumberOfColumnsInGrid());
        mMoviesGridRecyclerView.setLayoutManager(moviesLayoutManager);

        mMoviesGridRecyclerView.setHasFixedSize(true);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(moviesLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // todo add from submission
                // todo add restart loader with bundle to load note
                // todo endless scroll note
            }
        };

        moviesGridAdapter = new MoviesGridAdapter(this, this);
        mMoviesGridRecyclerView.setAdapter(moviesGridAdapter);

        progressBar = (ProgressBar) findViewById(R.id.moviesGridProgressBar);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_sort:
                SortOrderDialog sortOrderDialog = new SortOrderDialog();
                sortOrderDialog.show(getFragmentManager(), SORT_ORDER_DIALOG_TAG);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case MOVIES_GRID_LOADER_ID:
                return new MoviesGridLoader(this, this);
            default:
                throw new RuntimeException("Loader not implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        if (movies != null && !movies.isEmpty()) {
            moviesGridAdapter.setData(movies);
        } else {
            // todo handle no data. Show error message
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
    }

    @Override
    public void onMovieClick(int position) {
        Movie movie = moviesGridAdapter.getMovie(position);
        if (movie != null) {
            Intent movieDetailsActivityIntent = new Intent(MainActivity.this, MovieDetailsActivity.class);
            movieDetailsActivityIntent.putExtra(MOVIE_EXTRA, movie);
            startActivity(movieDetailsActivityIntent);
        }
    }

    @Override
    public void onSortOrderChange(int posInDialog) {
        Loader loader = getSupportLoaderManager().getLoader(MOVIES_GRID_LOADER_ID);
        MoviesGridLoader moviesGridLoader = (MoviesGridLoader) loader;
        moviesGridLoader.changeSortOrder(posInDialog);
    }

    @Override
    public void onStartLoadingMovies() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFinishLoadingMovies() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private int getNumberOfColumnsInGrid() {
        /**
         * Calculate number of columns in grid based on device width
         * */

        final int SCALING_FACTOR = 180;

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        int numberOfColumns = (int) (dpWidth / SCALING_FACTOR);

        return numberOfColumns;
    }
}
