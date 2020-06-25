package com.gibsoncodes.data.api

import com.gibsoncodes.data.models.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines")
    suspend fun getHeadlinesBySource(@Query("sources") source:String):NewsResponse

    @GET("everything")
    suspend fun getEveryNewsArticle(@Query("q")category:String):NewsResponse

    @GET("top-headlines")
    suspend fun getHeadlinesByCategory(@Query("category")category: String):NewsResponse



}