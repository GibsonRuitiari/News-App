package com.gibsoncodes.data.models

import com.google.gson.annotations.SerializedName

data class NewsResponse (@SerializedName("status") val status:String,
                        @SerializedName("articles")val articles:List<ArticlesResponse>)

data class ArticlesResponse(@SerializedName("author") val author:String?,
                            @SerializedName("title") val title:String?,
                            @SerializedName("description") val description:String,
                            @SerializedName("url") val url:String,
                            @SerializedName("urlToImage") val urlToImage:String,
                            @SerializedName("publishedAt") val publishedAt:String,
                            @SerializedName("source") val source:SourceResponse?)

data class SourceResponse(@SerializedName("id") val id:String?,
                          @SerializedName("name") val name:String?)