package ru.artempugachev.popularmovies.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Content provider for movies
 * Uses https://github.com/SimonVT/schematic
 */

@ContentProvider(authority = MovieProvider.AUTHORITY, database = MovieDatabase.class)
public class MovieProvider {
    public static final String AUTHORITY = "ru.artempugachev.popularmovies.data.MovieProvider";

    @TableEndpoint(table = MovieDatabase.MOVIES)
    public static class Movies {
        @ContentUri(
                path = "movies",
                type = "vnd.android.cursor.dir/list",
                defaultSort = MovieContract.MovieEntry.DATE_ADDED + " DESC")
        public static final Uri MOVIES = Uri.parse("content://" + AUTHORITY + "/movies");
    }
}
