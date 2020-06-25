package com.gibsoncodes.newsapp.common

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar

interface RecyclerViewListener{
    fun onClick(position:Int)
}

fun ShowSnackbar(view: View, msg:String){
    Snackbar.make(view,msg,Snackbar.LENGTH_LONG)
        .setTextColor(Color.WHITE)
        .setBackgroundTint(Color.BLACK)
        .show()
}