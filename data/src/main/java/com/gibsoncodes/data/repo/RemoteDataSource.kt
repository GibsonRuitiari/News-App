package com.gibsoncodes.data.repo

import com.gibsoncodes.data.api.ApiService
import com.gibsoncodes.data.mappers.toEntity
import com.gibsoncodes.data.models.Articles

/**
 * Responsible for fetching data from the network
 *
 */
class RemoteDataSourceRepo(private val apiService: ApiService){

    suspend fun getHeadlinesBySource(source:String):List<Articles>{
        val newsResponse = apiService.getHeadlinesBySource(source)
        val articles = mutableListOf<Articles>()
        newsResponse.articles.forEach {
           articles.add(it.toEntity())
        }
        return articles
    }
    suspend fun getHeadlinesByCategory(category:String):List<Articles>{
        val newsResponse =apiService.getHeadlinesByCategory(category)
        val articles= mutableListOf<Articles>()
        newsResponse.articles.forEach { articles.add(it.toEntity()) }
        return articles
    }
    suspend fun getEveryNewsArticle(category: String):List<Articles>{
        val newsResponse = apiService.getEveryNewsArticle(category)
        val articles= mutableListOf<Articles>()
        newsResponse.articles.forEach { articles.add(it.toEntity()) }
        return articles
    }
}