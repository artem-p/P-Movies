package ru.artempugachev.popularmovies.data;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

/**
 * Load movies from local collection in db
 */

public class FavoritesLoader extends MoviesLoader {
    public FavoritesLoader(Context context, MoviesLoadListener moviesLoadListener) {
        super(context);
        this.moviesLoadListener = moviesLoadListener;
    }

    @Override
    protected void onForceLoad() {
        moviesLoadListener.onStartLoadingMovies();

        AsyncTask<Void, Void, List<Movie>> moviesLoadTask = new AsyncTask<Void, Void, List<Movie>>() {
            @Override
            protected List<Movie> doInBackground(Void... params) {
//                Toast.makeText(getContext(), "Start load movies from db", Toast.LENGTH_SHORT).show();
                return null;
            }

            @Override
            protected void onPostExecute(List<Movie> movies) {
                moviesLoadListener.onFinishLoadingMovies();
                deliverResult(movies);
            }

            @Override
            protected void onCancelled() {
                moviesLoadListener.onFinishLoadingMovies();
                super.onCancelled();
            }
        };

        moviesLoadTask.execute();

        super.onForceLoad();
    }
}
