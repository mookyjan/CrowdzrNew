package com.example.crowdzrnew.feature.login

import com.example.crowdzrnew.AppPreference
import com.example.crowdzrnew.core.util.SchedulerProvider
import com.example.crowdzrnew.rest.GeneralRepository
import dagger.Module
import dagger.Provides

@Module
class LoginActivityModule {

    @Provides
    fun provideViewModel(schedulerProvider: SchedulerProvider,
                         generalRepository: GeneralRepository,
                         appPreference: AppPreference)
                         =LoginViewModel(schedulerProvider,
                         generalRepository,appPreference)
}