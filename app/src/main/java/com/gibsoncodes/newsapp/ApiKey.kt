package com.gibsoncodes.newsapp

import org.koin.dsl.module

/*val configModule = module{
    factory { BuildConfigConstants() }
}*/
class BuildConfigConstants{
    init {
        val api_= BuildConfig.API_KEY
        val url_= BuildConfig.API_URL
    }
}
