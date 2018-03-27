package ru.artempugachev.popularmovies.tmdb;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit-powered api client for tmdb
 */

public class TmdbApiClient {
    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    public TmdbApiInterface buildApiInterface() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(TmdbApiInterface.class);
    }

}
