package com.example.crowdzrnew.di

import com.example.crowdzrnew.feature.login.LoginActivity
import com.example.crowdzrnew.feature.login.LoginActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [(LoginActivityModule::class)])
    abstract fun bindLoginActivity():LoginActivity
}