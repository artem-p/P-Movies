package ru.artempugachev.popularmovies.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Database description.
 */

@Database(version = MovieDatabase.VERSION, packageName = "ru.artempugachev.popularmovies.data.gen")
public class MovieDatabase {
    public static final int VERSION = 1;

    @Table(MovieContract.MovieEntry.class)
    public static final String MOVIES = "movies";
}
