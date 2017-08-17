package ru.artempugachev.popularmovies.data;

import android.content.ContentValues;
import android.net.Uri;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Contract for storing movies in db
 */

public class MovieContract {
    public interface MovieEntry {
        @DataType(INTEGER)
        @PrimaryKey
        @AutoIncrement
        String _ID = "_id";

        @DataType(TEXT)
        @NotNull
        String TITLE = "title";

        @DataType(INTEGER)
        @NotNull
        String RELEASE_DATE = "release_date";

        @DataType(TEXT)
        @NotNull
        String MOVIE_ID = "movie_id";
    }
}
