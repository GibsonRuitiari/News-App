package com.gibsoncodes.data.mappers

import com.gibsoncodes.data.models.Articles
import com.gibsoncodes.data.models.ArticlesResponse
import com.gibsoncodes.data.models.Source


internal fun ArticlesResponse.toEntity():Articles{
    val sourceId:String?
    val sourceName:String?
    if(this.source?.id!=null){
        sourceId=this.source.id
    }else sourceId=null
    if(this.source?.name!=null){
        sourceName=this.source.name
    }else sourceName=null
    return Articles(this.author,this.title,this.description,this.urlToImage,this.url,
        this.publishedAt,
        Source(sourceId,sourceName))
}