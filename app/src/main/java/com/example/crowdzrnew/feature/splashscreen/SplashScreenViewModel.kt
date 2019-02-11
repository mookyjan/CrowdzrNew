package com.example.crowdzrnew.feature.splashscreen

import android.arch.lifecycle.ViewModel
import com.example.crowdzrnew.AppPreference
import com.example.crowdzrnew.core.Router
import com.example.crowdzrnew.core.event.StartActivityEvent
import com.example.crowdzrnew.core.event.StartActivityModel
import com.example.crowdzrnew.database.DatabaseRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class SplashScreenViewModel(
    private val appPreference: AppPreference,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    val startActivityEvent = StartActivityEvent()
    lateinit var splashScreenListener: SplashScreenListener

    fun redirect() {
        if (appPreference.isLoggedIn()) {
            startActivityEvent.value = StartActivityModel(Router.Destination.MAIN)
        } else {
            startActivityEvent.value = StartActivityModel(Router.Destination.LOGIN)
        }
    }


    fun getUserName(username: String) {
        databaseRepository.getUserEmail(username).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribeBy(
                onSuccess = {
                    if (it != null) {
                        redirect()
                    } else {
                        startActivityEvent.value = StartActivityModel(Router.Destination.LOGIN)
                    }
                },
                onError = {
                    startActivityEvent.value = StartActivityModel(Router.Destination.LOGIN)
                }
            )
    }

    public interface SplashScreenListener {
        fun showErrorDialog(msgId: Int, updateUrl: String? = null, message: String? = null)
        fun proceed()
    }
}