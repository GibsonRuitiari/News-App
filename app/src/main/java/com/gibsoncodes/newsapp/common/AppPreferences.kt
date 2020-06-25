package com.gibsoncodes.newsapp.common

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.lang.reflect.Type

val appPreferencesModule = module{
    single{AppPreferences(androidContext())}
}

class AppPreferences(val context: Context){
    companion object{
      const  val SharedPrefName="AppPreferences"
        const val viewPagerStatus="_viewPagerStatus"
        const val savedSourcesList="_savedSourcesList"
        const val loggedInName="_loggedInNameKey"
        const val loggedInEmail="_loggedInEmailKey"
        const val loggedInKey ="_loggedIn"
        const val isSourcesListSaved="_sourcesListSaved"
        const val Mode= Context.MODE_PRIVATE
        private val gson= Gson()
        private fun convertObjectToJson(obj:Any):String{
            return gson.toJson(obj)
        }
    }
   private val sharedPreferences= context.getSharedPreferences(SharedPrefName,Mode)

    private fun getSharedPreferences():SharedPreferences{
        return sharedPreferences
    }
    fun savedLoggedInName(key:String,name:String){
        val editor= getSharedPreferences().edit()
        editor.putString(key,name)
        editor.apply()
    }
    fun isSourcesListSaved(key:String,value:Boolean){
        val editor= getSharedPreferences().edit()
        editor.putBoolean(key,value)
        editor.apply()
    }
    fun retrieveSourcesListStatus(key:String):Boolean{
        return getSharedPreferences().getBoolean(key,false)
    }
    fun savedLoggedInEmail(key:String,email:String){
        val editor= getSharedPreferences().edit()
        editor.putString(key,email)
        editor.apply()
    }

    fun retrieveMemberName(key:String):String?{
        return getSharedPreferences().getString(key,null)
    }
    fun retrieveMemberEmail(key:String):String?{
        return getSharedPreferences().getString(key,null)
    }
    fun clearPreferences(){
        val editor= getSharedPreferences().edit()
        editor.clear()
        editor.apply()
    }
    fun saveLoggedInStatus(key:String,loggedIn:Boolean){
        val editor= getSharedPreferences().edit()
        editor.putBoolean(key,loggedIn)
        editor.apply()
    }
    fun retrieveLoggedInStatus(key:String):Boolean{
        return getSharedPreferences().getBoolean(key,false)
    }
    fun<T> saveNewsSourcesPreferences(key:String,collection: Collection<T>){
        val editor= getSharedPreferences().edit()
        val jsonString = convertObjectToJson(collection)
        editor.putString(key,jsonString)
        editor.apply()
    }
    fun <T> getNewsSourcesPreferences(key:String,type: Type):T?{
        val jsonString = getSharedPreferences().getString(key,null)
        return gson.fromJson(jsonString,type)
    }
    fun saveShowViewPagerStatus(key: String,value:Boolean){
        val editor= getSharedPreferences().edit()
        editor.putBoolean(key,value)
        editor.apply()
    }
    fun retrieveShowViewPagerStatus(key: String):Boolean{
        return getSharedPreferences().getBoolean(key,false)
    }
}