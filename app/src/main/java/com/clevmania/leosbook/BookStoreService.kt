package com.clevmania.leosbook

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author by Lawrence on 8/2/20.
 * for LeosBook
 */
const val storeBaseUrl = "https://www.googleapis.com/books/"

interface BookStoreService {
    companion object{
        operator fun invoke(): Retrofit {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(storeBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}