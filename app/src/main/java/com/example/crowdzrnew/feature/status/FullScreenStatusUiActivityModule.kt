package com.example.crowdzrnew.feature.status

import dagger.Module
import dagger.Provides

@Module
class FullScreenStatusUiActivityModule {
    @Provides
    fun provideViewModel() = FullScreenStatusViewModel()
}