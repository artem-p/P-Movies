package ru.artempugachev.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor;

        switch (match) {
            case MOVIES:
                cursor = db.query(MovieContract.MovieEntry.TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String where = MovieContract.MovieEntry.MOVIE_ID + "=?";
                String[] whereArgs = new String[]{id};

                cursor = db.query(MovieContract.MovieEntry.TABLE_NAME,
                        null, where, whereArgs, null, null, null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES:
                long id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieContract.MOVIES_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
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
