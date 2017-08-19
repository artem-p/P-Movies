package ru.artempugachev.popularmovies;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.Loader;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.artempugachev.popularmovies.data.Movie;
import ru.artempugachev.popularmovies.data.MovieResponse;
import ru.artempugachev.popularmovies.tmdb.TmdbApiClient;
import ru.artempugachev.popularmovies.tmdb.TmdbApiInterface;

/**
 * Fetch data from tmdb or local db and load it to activity
 */

public class MoviesGridLoader extends Loader<List<Movie>> {
    public interface MoviesLoadListener {
        void onStartLoadingMovies();
        void onFinishLoadingMovies();
    }


    public MoviesGridLoader(Context context, MoviesLoadListener moviesLoadListener, int pageNumber) {
        super(context);
        this.moviesLoadListener = moviesLoadListener;
        this.pageNumber = pageNumber;
    }

    private List<Movie> movies;
    private String sortOrderId = "popular";
    private MoviesLoadListener moviesLoadListener;
    private int pageNumber;

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
        if (this.sortOrderId.equals(getContext().getResources().getString(R.string.sort_order_id_favorites))) {
            // Favorites
            loadFromLocalDb();
        } else {
            // Popular or top rated
            loadFromTmdb();
        }

        super.onForceLoad();
    }

    private void loadFromLocalDb() {

    }


    private void loadFromTmdb() {
        TmdbApiClient tmdbApiClient = new TmdbApiClient();
        TmdbApiInterface tmdbApiInterface = tmdbApiClient.buildApiInterface();
        Call<MovieResponse> call = tmdbApiInterface.getMovies(sortOrderId, BuildConfig.TMDB_API_KEY, pageNumber);

        if (call != null) {
            moviesLoadListener.onStartLoadingMovies();
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    moviesLoadListener.onFinishLoadingMovies();
                    if (response.isSuccessful()) {
                        List<Movie> movies = response.body().getResults();
                        deliverResult(movies);
                    } else {
                        deliverResult(null);
                    }
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable throwable) {
                    moviesLoadListener.onFinishLoadingMovies();
                    deliverResult(null);
                }

            });
        }
    }


    public void changeSortOrder(int posInDialog) {
        this.pageNumber = 1;
        try {
            Resources resources = getContext().getResources();
            this.sortOrderId = resources.getStringArray(R.array.sort_orders_id)[posInDialog];
            forceLoad();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("No id for sort order with position " + posInDialog);
        }
    }
}
