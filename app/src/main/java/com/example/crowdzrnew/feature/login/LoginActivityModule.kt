package com.example.crowdzrnew.feature.login

import com.example.crowdzrnew.AppPreference
import com.example.crowdzrnew.AppResourceProvider
import com.example.crowdzrnew.core.util.SchedulerProvider
import com.example.crowdzrnew.database.DatabaseRepository
import com.example.crowdzrnew.rest.GeneralRepository
import com.example.crowdzrnew.rest.repositoroy.TokenRepository
import dagger.Module
import dagger.Provides

@Module
class LoginActivityModule {

    @Provides
    fun provideViewModel(schedulerProvider: SchedulerProvider,
                         tokenRepository: TokenRepository,
                         databaseRepository: DatabaseRepository,
                          appResourceProvider: AppResourceProvider,
                         appPreference: AppPreference)
                         =LoginViewModel(schedulerProvider,
                         tokenRepository,databaseRepository,appResourceProvider,appPreference)
}