package com.example.crowdzrnew.feature.splashscreen

import com.example.crowdzrnew.AppPreference
import com.example.crowdzrnew.database.DatabaseRepository
import dagger.Module
import dagger.Provides

@Module
class SplashScreenModule {

    @Provides
    fun provideViewModel(appPreference: AppPreference, databaseRepository: DatabaseRepository) =
        SplashScreenViewModel(appPreference, databaseRepository)
}