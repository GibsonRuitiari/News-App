package com.gibsoncodes.di.modules

import com.gibsoncodes.data.repo.RemoteDataSourceRepo
import org.koin.dsl.module

val repositoryModule= module{
single { RemoteDataSourceRepo(get()) }
}