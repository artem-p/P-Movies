package ru.artempugachev.popularmovies;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import ru.artempugachev.popularmovies.data.Movie;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {

    // todo should be different in landscape mode
    private final static int MOVIES_SPAN_COUNT = 2;

    private final static int MOVIES_GRID_LOADER_ID = 42;

    private MoviesGridAdapter moviesGridAdapter;


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

        GridLayoutManager moviesLayoutManager = new GridLayoutManager(this, MOVIES_SPAN_COUNT);
        mMoviesGridRecyclerView.setLayoutManager(moviesLayoutManager);

        mMoviesGridRecyclerView.setHasFixedSize(true);

        moviesGridAdapter = new MoviesGridAdapter(this);
        mMoviesGridRecyclerView.setAdapter(moviesGridAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case MOVIES_GRID_LOADER_ID:
                return new MoviesGridLoader(this);
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
}
