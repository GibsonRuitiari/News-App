package com.gibsoncodes.newsapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.gibsoncodes.newsapp.common.AppPreferences
import org.koin.dsl.module

val profileViewModelModule= module {
    single { ProfileViewModel(get()) }
}
class ProfileViewModel (private val appPreferences: AppPreferences):ViewModel(){
    private fun provideMemberName():String?{
       return appPreferences.retrieveMemberName(AppPreferences.loggedInName)
    }
    private fun provideMemberEmail():String?{
        return appPreferences.retrieveMemberEmail(AppPreferences.loggedInEmail)
    }
    var memberName= liveData {
        emit(provideMemberName())
    }
    var memberEmail= liveData {
        emit(provideMemberEmail())
    }
}