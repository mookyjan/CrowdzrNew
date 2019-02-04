package com.example.crowdzrnew.feature.login

import addam.com.my.skeletonApp.core.event.StartActivityEvent
import addam.com.my.skeletonApp.core.event.StartActivityModel
import android.arch.lifecycle.ViewModel
import com.example.crowdzrnew.AppPreference
import com.example.crowdzrnew.core.Router
import com.example.crowdzrnew.core.util.SchedulerProvider
import com.example.crowdzrnew.rest.GeneralRepository
import com.example.crowdzrnew.rest.model.SampleLoginResponse
import com.example.crowdzrnew.utilities.ObservableString
import com.github.ajalt.timberkt.Timber
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy

class LoginViewModel(private val schedulerProvider: SchedulerProvider,
                     private val generalRepository: GeneralRepository,
                     private val appPreference: AppPreference):ViewModel() {

    var username =ObservableString("")
     val startActivityEvent:StartActivityEvent= StartActivityEvent()

    private fun callLoginApi():Single<SampleLoginResponse>{
        return generalRepository.getLogin().compose(schedulerProvider.getSchedulersForSingle())
    }

    fun onLoginClicked(){
        callLoginApi().subscribeBy (onSuccess = {
            Timber.d{"sucess for Login"}
            appPreference.setLoggedIn(true)
            startActivityEvent.value= StartActivityModel(Router.Destination.MAIN,
                hashMapOf(Pair(Router.Parameter.USERNAME,it.username)))
        },onError = {
            Timber.e { it.message.toString() }
        })
    }
}