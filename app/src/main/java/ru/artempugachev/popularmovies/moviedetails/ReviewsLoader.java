package ru.artempugachev.popularmovies.moviedetails;

import android.content.Context;
import android.support.v4.content.Loader;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.artempugachev.popularmovies.BuildConfig;
import ru.artempugachev.popularmovies.movielist.api.Review;
import ru.artempugachev.popularmovies.movielist.api.ReviewResponse;
import ru.artempugachev.popularmovies.tmdb.TmdbApiClient;
import ru.artempugachev.popularmovies.tmdb.TmdbApiInterface;

/**
 * Loader for reviews
 */

public class ReviewsLoader extends Loader<List<Review>> {
    private int pageNumber;
    private List<Review> reviews;
    private String movieId;

    public ReviewsLoader(Context context, int pageNumber, String movieId) {
        super(context);
        this.pageNumber = pageNumber;
        this.movieId = movieId;
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

        if (movieId != null) {

            Call<ReviewResponse> call = tmdbApiInterface.getReviews(movieId, BuildConfig.TMDB_API_KEY);

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
        } else {
            deliverResult(null);
        }

        super.onForceLoad();
    }
}
