package com.gibsoncodes.newsapp.application

import android.app.Application
import com.gibsoncodes.di.modules.networkModule
import com.gibsoncodes.di.modules.repositoryModule
import com.gibsoncodes.newsapp.common.appPreferencesModule
import com.gibsoncodes.newsapp.repo.sourcesRepoModule
import com.gibsoncodes.newsapp.viewmodels.allNewsViewModelModule
import com.gibsoncodes.newsapp.viewmodels.headlinesViewModule
import com.gibsoncodes.newsapp.viewmodels.profileViewModelModule
import com.gibsoncodes.newsapp.viewmodels.sourcesViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class NewsApplication  : Application(){
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin{
            androidLogger()
            androidContext(this@NewsApplication)
            modules(listOf(networkModule, repositoryModule, headlinesViewModule,
                profileViewModelModule,
                allNewsViewModelModule, appPreferencesModule, sourcesViewModelModule,sourcesRepoModule))

        }
    }
}
