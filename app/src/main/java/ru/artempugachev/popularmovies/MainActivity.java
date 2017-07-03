package ru.artempugachev.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.artempugachev.popularmovies.data.Movie;
import ru.artempugachev.popularmovies.data.MoviesResponse;
import ru.artempugachev.popularmovies.tmdb.TmdbApiClient;
import ru.artempugachev.popularmovies.tmdb.TmdbApiInterface;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mMoviesGridRecyclerView;

    // todo should be different in landscape mode
    private final static int MOVIES_SPAN_COUNT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpViews();

        TmdbApiClient tmdbApiClient = new TmdbApiClient();
        TmdbApiInterface tmdbApiInterface = tmdbApiClient.buildApiInterface();

        Call<MoviesResponse> call = tmdbApiInterface.getPopularMovies(BuildConfig.TMDB_API_KEY);

        // todo add work with adapter as described here http://www.androidhive.info/2016/05/android-working-with-retrofit-http-library/
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movie> movies = response.body().getResults();
                Movie first = movies.get(0);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable throwable) {
                // todo onFailure
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    private void setUpViews() {
        mMoviesGridRecyclerView = (RecyclerView) findViewById(R.id.rv_movies_grid);

        GridLayoutManager moviesLayoutManager = new GridLayoutManager(this, MOVIES_SPAN_COUNT);
        mMoviesGridRecyclerView.setLayoutManager(moviesLayoutManager);

        mMoviesGridRecyclerView.setHasFixedSize(true);

        MoviesGridAdapter moviesGridAdapter = new MoviesGridAdapter();
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
}
