package ru.artempugachev.popularmovies.data;

import android.provider.BaseColumns;


/**
 * Contract for storing movies in db
 */

public final class MovieContract {
    private MovieContract() {

    }

    private static final String TEXT_NOT_NULL = " TEXT NOT NULL, ";

    public static final class MovieEntry implements BaseColumns {
        private static final String TABLE_NAME = "movies";
        public static final String _ID = "_id";
        public static final String TITLE = "title";
        public static final String RELEASE_DATE = "release_date";
        public static final String MOVIE_ID = "movie_id";
        public static final String POSTER_PATH = "poster_path";
        public static final String OVERVIEW = "overview";
        public static final String BACKDROP_PATH = "backdrop_path";
        public static final String VOTE_AVERAGE = "vote_average";
    }

    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
            MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MovieEntry.MOVIE_ID + TEXT_NOT_NULL +
            MovieEntry.TITLE + TEXT_NOT_NULL +
            MovieEntry.RELEASE_DATE + TEXT_NOT_NULL +
            MovieEntry.POSTER_PATH + TEXT_NOT_NULL +
            MovieEntry.OVERVIEW + TEXT_NOT_NULL +
            MovieEntry.BACKDROP_PATH + TEXT_NOT_NULL +
            MovieEntry.VOTE_AVERAGE + " REAL NOT NULL" + ");";
}
