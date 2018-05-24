package ru.artempugachev.popularmovies.movielist;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.artempugachev.popularmovies.R;
import ru.artempugachev.popularmovies.movielist.api.Movie;
import ru.artempugachev.popularmovies.data.MovieContract;

/**
 * Fetch data from tmdb or local db and load it to activity
 */

public class MovieListLoader extends Loader<List<Movie>> {
    private static final String TAG = MovieListLoader.class.getSimpleName();

    public interface MoviesLoadListener {
        void onStartLoadingMovies();
        void onFinishLoadingMovies();
    }


    public MovieListLoader(Context context, MoviesLoadListener moviesLoadListener, int pageNumber, String sortOrderId) {
        super(context);
        this.moviesLoadListener = moviesLoadListener;
        this.pageNumber = pageNumber;
        this.sortOrderId = sortOrderId;
    }

    private List<Movie> movies;
    private String sortOrderId = MovieListActivity.Companion.getDEFAULT_SORT_ORDER();
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

//        super.onForceLoad();
    }

    private void loadFromLocalDb() {

        AsyncTask<Void, Void, List<Movie>> getFavoriteMoviesTask = new AsyncTask<Void, Void, List<Movie>>() {
            @Override
            protected void onPreExecute() {
                moviesLoadListener.onStartLoadingMovies();
                super.onPreExecute();
            }

            @Override
            protected List<Movie> doInBackground(Void... params) {
                List<Movie> movies = new ArrayList<Movie>();

                try {
                    Cursor cursor = getContext().getContentResolver().query(MovieContract.MOVIES_URI,
                            null, null, null, MovieContract.MovieEntry._ID);

                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            String tmbdId = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_ID));
                            String title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.TITLE));
                            String overview = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.OVERVIEW));
                            String posterPath = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.POSTER_PATH));
                            String backdropPath = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.BACKDROP_PATH));
                            String releaseDate = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.RELEASE_DATE));
                            Double voteAverage = cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.VOTE_AVERAGE));

                            Movie movie = new Movie(posterPath, overview, releaseDate, tmbdId, title, backdropPath, voteAverage);

                            movies.add(movie);
                        }

                        cursor.close();
                    }

                } catch (Exception e) {
                    Log.e(TAG, "Cannot get favorite movies from db");
                }

                return movies;
            }

            @Override
            protected void onPostExecute(List<Movie> movies) {
                moviesLoadListener.onFinishLoadingMovies();
                deliverResult(movies);
                super.onPostExecute(movies);
            }

            @Override
            protected void onCancelled() {
                moviesLoadListener.onFinishLoadingMovies();
                super.onCancelled();
            }
        };

        getFavoriteMoviesTask.execute();
    }


    private void loadFromTmdb() {
//        TmdbApiClient tmdbApiClient = new TmdbApiClient();
//        TmdbApiInterface tmdbApiInterface = tmdbApiClient.buildApiInterface();
//        Call<MovieResponse> call = tmdbApiInterface.getMovies(sortOrderId, BuildConfig.TMDB_API_KEY, pageNumber);
//
//        if (call != null) {
//            moviesLoadListener.onStartLoadingMovies();
//            call.enqueue(new Callback<MovieResponse>() {
//                @Override
//                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
//                    moviesLoadListener.onFinishLoadingMovies();
//                    if (response.isSuccessful()) {
//                        List<Movie> movies = response.body().getResults();
//                        deliverResult(movies);
//                    } else {
//                        deliverResult(null);
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<MovieResponse> call, Throwable throwable) {
//                    moviesLoadListener.onFinishLoadingMovies();
//                    deliverResult(null);
//                }
//
//            });
//        }
    }


    public void changeSortOrder(String sortOrderId) {
        this.pageNumber = 1;
        try {
            Resources resources = getContext().getResources();
            this.sortOrderId = sortOrderId;
            forceLoad();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("No id for sort order with id " + sortOrderId);
        }
    }
}
