package com.gibsoncodes.newsapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.gibsoncodes.data.repo.RemoteDataSourceRepo
import com.gibsoncodes.newsapp.common.NetworkResponseHandler
import com.gibsoncodes.newsapp.common.Resource
import com.gibsoncodes.newsapp.common.toArticlePresentation
import com.gibsoncodes.newsapp.model.ArticlePresentation
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import timber.log.Timber


val allNewsViewModelModule = module{
    single{ AllNewsViewModel(get())}
}
class AllNewsViewModel(private val repo: RemoteDataSourceRepo): ViewModel() {
    private val responseHandler = NetworkResponseHandler()
    private val categoryList= listOf("politics","music","technology")
private suspend fun getNewsArticlesByCategory(category: String):Resource<List<ArticlePresentation>>{
    return try{
      return responseHandler.handleSuccess(repo.getEveryNewsArticle(category).map { it.toArticlePresentation() })
    }catch (e:Exception){
        Timber.e("An error occurred: ${e}")
        responseHandler.handleException(e)
    }
}
    val sportsArticles= liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        emit(getNewsArticlesByCategory(categoryList[0]))
    }
    val musicArticles= liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        emit(getNewsArticlesByCategory(categoryList[1]))
    }
    val technologyArticles= liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        emit(getNewsArticlesByCategory(categoryList[2]))
    }


}