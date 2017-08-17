package ru.artempugachev.popularmovies.data;

import android.content.Context;

/**
 * Load movies from local collection in db
 */

public class FavoritesLoader extends MoviesLoader {
    public FavoritesLoader(Context context) {
        super(context);
    }

    @Override
    protected void onForceLoad() {
        moviesLoadListener.onStartLoadingMovies();

        super.onForceLoad();
    }
}
