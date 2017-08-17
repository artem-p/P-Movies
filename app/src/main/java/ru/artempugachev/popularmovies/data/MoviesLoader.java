package ru.artempugachev.popularmovies.data;

import android.content.Context;
import android.support.v4.content.Loader;

import java.util.List;

/**
 * Base loader for movies.
 * Movies can be load right from TMDB or from local collection in db.
 */

public abstract class MoviesLoader extends Loader<List<Movie>> {
    // Use listener to show progress bar in activity when loading.
    public interface MoviesLoadListener {
        void onStartLoadingMovies();
        void onFinishLoadingMovies();
    }

    protected List<Movie> movies;
    protected String sortOrderId = "popular";
    protected MoviesLoadListener moviesLoadListener;

    public MoviesLoader(Context context) {
        super(context);
    }

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
}
