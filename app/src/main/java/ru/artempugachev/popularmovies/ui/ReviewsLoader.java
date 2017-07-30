package ru.artempugachev.popularmovies.ui;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.Loader;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.artempugachev.popularmovies.BuildConfig;
import ru.artempugachev.popularmovies.MovieDetailsActivity;
import ru.artempugachev.popularmovies.R;
import ru.artempugachev.popularmovies.data.Movie;
import ru.artempugachev.popularmovies.data.MovieResponse;
import ru.artempugachev.popularmovies.data.Review;
import ru.artempugachev.popularmovies.data.ReviewResponse;
import ru.artempugachev.popularmovies.tmdb.TmdbApiClient;
import ru.artempugachev.popularmovies.tmdb.TmdbApiInterface;

/**
 * Loader for reviews
 */

public class ReviewsLoader extends Loader<List<Review>> {
    private int pageNumber;
    private List<Review> reviews;

    public ReviewsLoader(Context context, int pageNumber) {
        super(context);
        this.pageNumber = pageNumber;
    }

    @Override
    protected void onStartLoading() {
        if (reviews != null) {
            deliverResult(reviews);
        } else {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(List<Review> data) {
        reviews = data;
        super.deliverResult(reviews);
    }


    @Override
    protected void onForceLoad() {
        TmdbApiClient tmdbApiClient = new TmdbApiClient();
        TmdbApiInterface tmdbApiInterface = tmdbApiClient.buildApiInterface();

        // todo we need id here. From bundle
        Call<ReviewResponse> call = tmdbApiInterface.getReviews();

        if (call != null) {
            call.enqueue(new Callback<ReviewResponse>() {
                @Override
                public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                    if (response.isSuccessful()) {
                        List<Review> reviews = response.body().getResults();
                        deliverResult(reviews);
                    } else {
                        deliverResult(null);
                    }
                }

                @Override
                public void onFailure(Call<ReviewResponse> call, Throwable throwable) {
                    deliverResult(null);
                }

            });
        }

        super.onForceLoad();
    }
}
