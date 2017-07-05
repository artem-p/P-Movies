package ru.artempugachev.popularmovies;

import android.content.Context;
import android.support.v4.content.Loader;

import java.util.List;

import ru.artempugachev.popularmovies.data.Movie;

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
}
