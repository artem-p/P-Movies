package ru.artempugachev.popularmovies;

import android.content.Context;
import android.support.v4.content.Loader;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.artempugachev.popularmovies.data.Movie;
import ru.artempugachev.popularmovies.data.MoviesResponse;
import ru.artempugachev.popularmovies.tmdb.TmdbApiClient;
import ru.artempugachev.popularmovies.tmdb.TmdbApiInterface;

/**
 * We use loader to fetch tmdb data and load it to activity
 */

public class MoviesGridLoader extends Loader<List<Movie>> {
    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     */
    public MoviesGridLoader(Context context) {
        super(context);
    }

    private List<Movie> movies;

    @Override
    protected void onStartLoading() {
        if (movies != null) {
            deliverResult(movies);
        } else {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(List<Movie> data) {
        movies = data;
        super.deliverResult(movies);
    }

    @Override
    protected void onForceLoad() {
        TmdbApiClient tmdbApiClient = new TmdbApiClient();
        TmdbApiInterface tmdbApiInterface = tmdbApiClient.buildApiInterface();

        Call<MoviesResponse> call = tmdbApiInterface.getPopularMovies(BuildConfig.TMDB_API_KEY);

        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()) {
                    List<Movie> movies = response.body().getResults();
                    deliverResult(movies);
                } else {
                    deliverResult(null);
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable throwable) {
                deliverResult(null);
            }

        });
        super.onForceLoad();
    }
}