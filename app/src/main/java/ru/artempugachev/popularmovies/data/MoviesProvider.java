package ru.artempugachev.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


/**
 * Content provider for movies
 */

public class MoviesProvider extends ContentProvider {
    private DBHelper mDbHelper;
    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}


//@ContentProvider(authority = MoviesProvider.AUTHORITY, database = MovieDatabase.class,
//        packageName = "ru.artempugachev.popularmovies.data.gen")
//public class MoviesProvider {
//    public static final String AUTHORITY = "ru.artempugachev.popularmovies";
//    public static final String PATH_MOVIES = "movies";
//    private static final String TYPE_DIR = "vnd.android.cursor.dir/list";
//    private static final String TYPE_ITEM = "vnd.android.cursor.item/list";
//
//    @TableEndpoint(table = MovieDatabase.MOVIES)
//    public static class Movies {
//        @ContentUri(
//                path = PATH_MOVIES,
//                type = TYPE_DIR,
//                defaultSort = MovieContract.MovieEntry._ID + " DESC")
//        public static final Uri MOVIES = Uri.parse("content://" + AUTHORITY + "/" + PATH_MOVIES);
//
//
//        @InexactContentUri(
//                path = PATH_MOVIES + "/#",
//                name = "MOVIE_ID",
//                type = TYPE_ITEM,
//                whereColumn = MovieContract.MovieEntry.MOVIE_ID,
//                pathSegment = 1)
//
//        public static Uri withId(String movieId) {
//            return Uri.parse("content://" + AUTHORITY + "/" + PATH_MOVIES + "/" + movieId);
//        }
//    }
//}
