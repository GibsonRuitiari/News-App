package com.gibsoncodes.newsapp.common

import com.gibsoncodes.data.models.Articles
import com.gibsoncodes.newsapp.model.ArticlePresentation
import com.gibsoncodes.newsapp.model.SourcePresentation

internal fun Articles.toArticlePresentation():ArticlePresentation{
    return ArticlePresentation(this.author,this.title,this.description,this.urlToImage,this.url,
        this.publishedAt,SourcePresentation(this.source?.id,this.source?.name))
}