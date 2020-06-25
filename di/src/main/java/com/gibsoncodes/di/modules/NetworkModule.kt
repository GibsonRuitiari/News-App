package com.gibsoncodes.di.modules

import com.gibsoncodes.data.api.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule= module {
  single { provideService(get())}
    single { provideRetrofit(get(), provideBaseUrl() )}
    single { provideOkHttpClient() }
}

private fun provideOkHttpClient():OkHttpClient{
    val httpLoggingInterceptor= HttpLoggingInterceptor()
    httpLoggingInterceptor.level= HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder()
        .connectTimeout(60,TimeUnit.SECONDS)
        .readTimeout(60,TimeUnit.SECONDS)
        .writeTimeout(60,TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor {
            val req= it.request()
            val modifiedUrl=req.url().newBuilder().addQueryParameter("language","en").build()
            val originalRequest = it.request().newBuilder()
                .addHeader("Content-Type","application/json")
                .addHeader("Accept-Type","application/json")
                .addHeader("Authorization","37c47c8e756746d88ef1a3ef11076cd8")// Your api key here
                .url(modifiedUrl)
                .build()
            return@addInterceptor it.proceed(originalRequest)
        }.build()
}
private fun provideRetrofit(client: OkHttpClient,appUrl:String):Retrofit{
    return Retrofit.Builder()
        .baseUrl(appUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
private fun provideBaseUrl():String{
    return "https://newsapi.org/v2/"
}
private fun provideService(retrofit: Retrofit):ApiService{
    return retrofit.create(ApiService::class.java)
}