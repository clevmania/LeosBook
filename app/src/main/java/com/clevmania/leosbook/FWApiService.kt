package com.clevmania.leosbook

import com.clevmania.leosbook.constants.AppKeys
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author by Lawrence on 8/3/20.
 * for LeosBook
 */
const val baseUrl = "https://api.flutterwave.com/"

interface FWApiService {
    companion object{
        operator fun invoke(): Retrofit {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor {
                    val request = it.request().newBuilder()
                        .addHeader("Authorization","Bearer ${AppKeys.SECRET_KEY}")
                        .build()

                    it.proceed(request = request)
                }
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}