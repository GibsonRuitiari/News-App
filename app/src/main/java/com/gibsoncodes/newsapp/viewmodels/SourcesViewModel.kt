package com.gibsoncodes.newsapp.viewmodels

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.gibsoncodes.newsapp.common.Resource
import com.gibsoncodes.newsapp.repo.SourcesRepo
import org.koin.dsl.module

val sourcesViewModelModule= module {
    single { SourcesViewModel(get()) }
}

class SourcesViewModel(private val repo: SourcesRepo):ViewModel(){

    var sourcesList= liveData {
        emit(repo.returnSourcesList())
    }
}