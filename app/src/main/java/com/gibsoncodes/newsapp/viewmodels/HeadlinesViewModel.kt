package com.gibsoncodes.newsapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.gibsoncodes.data.models.Articles
import com.gibsoncodes.data.repo.RemoteDataSourceRepo
import com.gibsoncodes.newsapp.common.AppPreferences
import com.gibsoncodes.newsapp.common.NetworkResponseHandler
import com.gibsoncodes.newsapp.common.Resource
import com.gibsoncodes.newsapp.common.toArticlePresentation
import com.gibsoncodes.newsapp.model.ArticlePresentation
import com.gibsoncodes.newsapp.model.SourcePresentation
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject
import timber.log.Timber
import java.lang.reflect.Type

val headlinesViewModule = module{
    single{ HeadlinesBySourceViewModel(get(),get())}
}
class HeadlinesBySourceViewModel(private val repo: RemoteDataSourceRepo,private val appPreferences: AppPreferences):ViewModel() {


private val responseHandler = NetworkResponseHandler()
   private val categoryList= listOf("politics","music","technology")
    private suspend fun getHeadlinesBySource():Resource<List<ArticlePresentation>>{
       // val appPreferences:AppPreferences by inject(AppPreferences::class.java)
        return try{
            val type: Type = object:TypeToken<List<SourcePresentation>>(){}.type
            val headlinesList = mutableListOf<Articles>()
            val sourcesList:List<SourcePresentation>? = appPreferences.getNewsSourcesPreferences(AppPreferences.savedSourcesList,type)
             sourcesList?.let{
                 when {
                     it.isNotEmpty() -> it.forEach { source->
                         headlinesList.addAll(repo.getHeadlinesBySource(source.id!!))
                     }
                     else -> {
                         headlinesList.addAll(repo.getHeadlinesBySource("cnn")) // default source
                         Timber.d("The sources list is empty!!!")
                     }
                 }
             }
            return responseHandler.handleSuccess(
                headlinesList.map { it.toArticlePresentation() })
        }catch (e:Exception){
            Timber.e("An error occurred: ${e}")
            responseHandler.handleException(e)
        }
    }

private suspend fun getHeadlinesByCategory(category: String):Resource<List<ArticlePresentation>>{
    return try{
        return responseHandler.handleSuccess(repo.getHeadlinesByCategory(category).map { it.toArticlePresentation() })
    }catch(e:Exception){
        Timber.e("An error occurred: ${e}")
        responseHandler.handleException(e)
    }
}


    var sportsHeadlines= liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        emit(getHeadlinesByCategory(categoryList[0]))
    }
    var politicsHeadlines= liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        emit(getHeadlinesByCategory(categoryList[1]))
    }
    var technologyHeadlines= liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        emit(getHeadlinesByCategory(categoryList[2]))
    }

    var headlinesBySourcesList = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        emit(getHeadlinesBySource())
    }
}
