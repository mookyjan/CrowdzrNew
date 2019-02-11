package com.example.crowdzrnew.di

import com.example.crowdzrnew.feature.login.LoginActivity
import com.example.crowdzrnew.feature.login.LoginActivityModule
import com.example.crowdzrnew.feature.splashscreen.SplashScreenActivity
import com.example.crowdzrnew.feature.splashscreen.SplashScreenModule
import com.example.crowdzrnew.feature.status.FullScreenStatusUiActivity
import com.example.crowdzrnew.feature.status.FullScreenStatusUiActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [(SplashScreenModule::class)])
    abstract fun bindSplashScreenActivity(): SplashScreenActivity

    @ContributesAndroidInjector(modules = [(LoginActivityModule::class)])
    abstract fun bindLoginActivity():LoginActivity

    @ContributesAndroidInjector(modules = [(FullScreenStatusUiActivityModule::class)])
    abstract fun bindFullScreenStatusActivity(): FullScreenStatusUiActivity
}