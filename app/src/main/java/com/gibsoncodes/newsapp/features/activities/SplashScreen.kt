package com.gibsoncodes.newsapp.features.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gibsoncodes.newsapp.R
import com.gibsoncodes.newsapp.common.AppPreferences
import com.gibsoncodes.newsapp.common.CropCircleTransformation
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_splash_screen.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class SplashScreen : AppCompatActivity() {
    private val appPreferences: AppPreferences by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Picasso.with(this@SplashScreen)
            .load(R.drawable.splash_bg_one)
            .fit()
            .transform(CropCircleTransformation())
            .centerCrop()
            .into(logo_one)
        Picasso.with(this@SplashScreen)
            .load(R.drawable.splash_bg_two)
            .fit()
            .transform(CropCircleTransformation())
            .centerCrop()
            .into(logo_two)
        Picasso.with(this@SplashScreen)
            .load(R.drawable.splash_bg_three)
            .fit()
            .transform(CropCircleTransformation())
            .centerCrop()
            .into(logo_three)
        Picasso.with(this@SplashScreen)
            .load(R.drawable.splash_bg_four)
            .fit()
            .transform(CropCircleTransformation())
            .centerCrop()
            .into(logo_four)
        val job = CoroutineScope(Dispatchers.Main)
        appPreferences.saveLoggedInStatus(AppPreferences.loggedInKey,true)

        job.launch {
            val loggedIn = appPreferences.retrieveLoggedInStatus(AppPreferences.loggedInKey)
            val vpStatus =
                appPreferences.retrieveShowViewPagerStatus(AppPreferences.viewPagerStatus)
            val sourcesList= appPreferences.retrieveSourcesListStatus(AppPreferences.isSourcesListSaved)
            delay(4500L)
            startActivity(Intent(Intent(this@SplashScreen, SignUpActivity::class.java)))
            if (vpStatus) {
                if (loggedIn) {
                    if (sourcesList){
                        startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                        finish()
                    }else{
                        startActivity(Intent(this@SplashScreen, NewsSources::class.java))
                        finish()
                    }
                } else {
                    startActivity(Intent(this@SplashScreen, SignUpActivity::class.java))
                    finish()
                }
            } else {
                startActivity(Intent(this@SplashScreen, ViewPagerActivity::class.java))
                finish()
            }

        }

    }
}