package ru.artempugachev.popularmovies.data;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.Loader;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.artempugachev.popularmovies.BuildConfig;
import ru.artempugachev.popularmovies.R;
import ru.artempugachev.popularmovies.data.Movie;
import ru.artempugachev.popularmovies.data.MovieResponse;
import ru.artempugachev.popularmovies.data.MoviesLoader;
import ru.artempugachev.popularmovies.tmdb.TmdbApiClient;
import ru.artempugachev.popularmovies.tmdb.TmdbApiInterface;

/**
 * We use loader to fetch tmdb data and load it to activity
 */

public class TmdbMoviesLoader extends MoviesLoader {

    public TmdbMoviesLoader (Context context, MoviesLoadListener moviesLoadListener, int pageNumber) {
        super(context);
        this.moviesLoadListener = moviesLoadListener;
        this.pageNumber = pageNumber;
    }


    private int pageNumber;
    private String sortOrderId = "popular";


    @Override
    protected void onForceLoad() {
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

        super.onForceLoad();
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
