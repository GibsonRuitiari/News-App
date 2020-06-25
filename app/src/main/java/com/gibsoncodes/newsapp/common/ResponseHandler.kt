package com.gibsoncodes.newsapp.common

import retrofit2.HttpException
import java.net.SocketTimeoutException


enum class ErrorCode(val code:Int){
    SocketTimeOut(-1)
}

 class NetworkResponseHandler{
    fun <T:Any> handleSuccess(data:T):Resource<T>{
        return Resource.success(data)
    }
    fun <T:Any> handleException(e:Exception):Resource<T>{
        return when(e){
            is HttpException-> Resource.error(parseErrorMessage(e.code()),null)
            is SocketTimeoutException-> Resource.error(parseErrorMessage(ErrorCode.SocketTimeOut.code),null)
            else -> Resource.error(parseErrorMessage(Int.MAX_VALUE),null)
        }
    }
    private fun parseErrorMessage(code:Int):String{
        return when(code){
            ErrorCode.SocketTimeOut.code-> "Timeout"
            401-> "Unauthorised!Please ensure you have an api key "
            404->"Not found!!Ensure the url is correct"
            else->"Please check your internet connection and try again :-("
        }
    }
}
