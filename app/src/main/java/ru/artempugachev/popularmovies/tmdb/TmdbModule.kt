package ru.artempugachev.popularmovies.tmdb

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@Module
class TmdbModule(private val apiKey: String) {
    @Provides
    fun provideClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

        // add api key to queries
        val apiKeyInterceptor = Interceptor {
            chain ->
            var request = chain.request()
            val url = request.url().newBuilder().addQueryParameter("api_key", apiKey).build()
            request = request.newBuilder().url(url).build()
            chain.proceed(request)
        }


        return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(apiKeyInterceptor)
                .build()
    }


    @Provides
    fun provideRetrofit(baseURL: String, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseURL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }


    @Provides
    fun provideApiService(): TmdbApiInterface {
        return provideRetrofit(BASE_URL, provideClient()).create(TmdbApiInterface::class.java)
    }


    companion object {
        private val BASE_URL = "http://api.themoviedb.org/3/"
    }
}