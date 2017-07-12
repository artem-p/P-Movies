package ru.artempugachev.popularmovies;

import android.content.Context;
import android.content.res.Resources;
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
    public interface MoviesLoadListener {
        public void onStartLoadingMovies();
        public void onFinishLoadingMovies();
    }


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
    public MoviesGridLoader(Context context, MoviesLoadListener moviesLoadListener) {
        super(context);
        this.moviesLoadListener = moviesLoadListener;
    }

    private List<Movie> movies;
    private String sortOrderId = "popular";
    private MoviesLoadListener moviesLoadListener;

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

    /**
     * Get retrofit api call for movies
     * */
    private Call<MoviesResponse> getTmdbApiCall() {
        TmdbApiClient tmdbApiClient = new TmdbApiClient();
        TmdbApiInterface tmdbApiInterface = tmdbApiClient.buildApiInterface();

        Call<MoviesResponse> call = null;
        if (sortOrderId.equals(getContext().getString(R.string.sort_order_id_popular))) {
            // load most popular
            call = tmdbApiInterface.getPopularMovies(BuildConfig.TMDB_API_KEY);
        } else if (sortOrderId.equals(getContext().getString(R.string.sort_order_id_top_rated))) {
            // load top rated
            call = tmdbApiInterface.getTopRatedMovies(BuildConfig.TMDB_API_KEY);
        }

        return call;
    }

    @Override
    protected void onForceLoad() {
        Call<MoviesResponse> call = getTmdbApiCall();

        if (call != null) {
            moviesLoadListener.onStartLoadingMovies();
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    moviesLoadListener.onFinishLoadingMovies();
                    if (response.isSuccessful()) {
                        List<Movie> movies = response.body().getResults();
                        deliverResult(movies);
                    } else {
                        deliverResult(null);
                    }
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable throwable) {
                    moviesLoadListener.onFinishLoadingMovies();
                    deliverResult(null);
                }

            });
        }

        super.onForceLoad();
    }

    public void changeSortOrder(int posInDialog) {
        try {
            Resources resources = getContext().getResources();
            this.sortOrderId = resources.getStringArray(R.array.sort_orders_id)[posInDialog];
            forceLoad();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("No id for sort order with position " + posInDialog);
        }
    }
}
