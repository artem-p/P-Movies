package ru.artempugachev.popularmovies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import ru.artempugachev.popularmovies.data.Movie;

public class MovieDetailsActivity extends AppCompatActivity {
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Movie movie = getIntent().getParcelableExtra(MainActivity.MOVIE_EXTRA);

        setUpViews();
        setData(movie);
    }

    private void setUpViews() {
        titleTextView = (TextView) findViewById(R.id.details_title);
    }

    /**
     * Set data from parcel extra to views
     * */
    private void setData(Movie movie) {
        titleTextView.setText(movie.getTitle());
    }
}
