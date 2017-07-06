package ru.artempugachev.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.artempugachev.popularmovies.data.Movie;

public class MovieDetailsActivity extends AppCompatActivity {
    private TextView titleTextView;
    private TextView yearTextView;
    private TextView overviewTextView;
    private TextView ratingTextView;
    private ImageView posterImageView;

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
        yearTextView = (TextView) findViewById(R.id.details_year);
        overviewTextView = (TextView) findViewById(R.id.details_overview);
        ratingTextView = (TextView) findViewById(R.id.details_rating);
        posterImageView = (ImageView) findViewById(R.id.details_poster);
    }

    /**
     * Set data from parcel extra to views
     * */
    private void setData(Movie movie) {
        titleTextView.setText(movie.getTitle());
        yearTextView.setText(movie.getReleaseDate());
        overviewTextView.setText(movie.getOverview());
        ratingTextView.setText(movie.getRating());

        Picasso.with(this).load(movie.getFullPosterPath()).into(posterImageView);
    }
}
