package com.gibsoncodes.newsapp.repo

import android.content.Context
import com.gibsoncodes.newsapp.R
import com.gibsoncodes.newsapp.common.Resource
import com.gibsoncodes.newsapp.model.SourcePresentation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import timber.log.Timber
import java.lang.reflect.Type

val sourcesRepoModule= module {
    single { SourcesRepo(androidContext()) }
}
class SourcesRepo(private val context:Context){
   fun returnSourcesList():List<SourcePresentation>{
     val sources= readJsonFile()
     val gson= Gson()
     val type:Type= object :TypeToken<List<SourcePresentation>>() {}.type
    return gson.fromJson(sources,type)
    }
    private fun readJsonFile():String? {
        var jsonString:String?=null
          try {
            jsonString= context.resources.openRawResource(R.raw.sources).bufferedReader().use {
                it.readText()
            }
            return jsonString
        }catch (e:Exception){
            Timber.e("An error occurred while reading the json file: ${e}")
        }
       return jsonString
    }
}