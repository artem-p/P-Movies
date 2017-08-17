package ru.artempugachev.popularmovies.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Content provider for movies
 * Uses https://github.com/SimonVT/schematic
 */

@ContentProvider(authority = MoviesProvider.AUTHORITY, database = MovieDatabase.class,
        packageName = "ru.artempugachev.popularmovies.data.gen")
public class MoviesProvider {
    public static final String AUTHORITY = "ru.artempugachev.popularmovies";
    public static final String PATH_MOVIES = "movies";
    private static final String TYPE_DIR = "vnd.android.cursor.dir/list";
    private static final String TYPE_ITEM = "vnd.android.cursor.item/list";

    @TableEndpoint(table = MovieDatabase.MOVIES)
    public static class Movies {
        @ContentUri(
                path = PATH_MOVIES,
                type = TYPE_DIR,
                defaultSort = MovieContract.MovieEntry._ID + " DESC")
        public static final Uri MOVIES = Uri.parse("content://" + AUTHORITY + "/" + PATH_MOVIES);


        @InexactContentUri(
                path = PATH_MOVIES + "/#",
                name = "MOVIE_ID",
                type = TYPE_ITEM,
                whereColumn = MovieContract.MovieEntry.MOVIE_ID,
                pathSegment = 1)

        public static Uri withId(String movieId) {
            return Uri.parse("content://" + AUTHORITY + "/" + PATH_MOVIES + "/" + movieId);
        }
    }
}
