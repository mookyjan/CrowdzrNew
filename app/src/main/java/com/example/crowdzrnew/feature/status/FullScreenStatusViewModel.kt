package com.example.crowdzrnew.feature.status

import android.arch.lifecycle.ViewModel
import com.example.crowdzrnew.core.Router
import com.example.crowdzrnew.core.event.FinishActivityEvent
import com.example.crowdzrnew.core.event.GenericSingleEvent
import com.example.crowdzrnew.core.event.StartActivityEvent
import com.example.crowdzrnew.core.event.StartActivityModel
import com.example.crowdzrnew.core.ui.FullScreenType
import com.example.crowdzrnew.core.ui.FullScreenUi

class FullScreenStatusViewModel :ViewModel() {
    val loginSuccessEvent: GenericSingleEvent = GenericSingleEvent()
    lateinit var fullScreenUI: FullScreenUi
    val finishActivityEvent: FinishActivityEvent = FinishActivityEvent()
    val startActivityEvent: StartActivityEvent = StartActivityEvent()
    private var checkLoginStatus :  Boolean = false
    val backPressEvent: GenericSingleEvent = GenericSingleEvent()


    fun onButtonClick() {

         if (fullScreenUI.screenType == FullScreenType.NOCONNECTION) {
            backPressEvent.postValue(true)
        } else if (fullScreenUI.screenType == FullScreenType.SERVERERROR) {
            backPressEvent.postValue(true)
        } else if (fullScreenUI.screenType == FullScreenType.ACCOUNTCREATED){
            //startActivityEvent.value = StartActivityModel(Router.Destination.MAIN, hashMapOf(Pair(Router.Parameter.REFRESH_TOKEN, true)), clearHistory = false)
        }
    }

    fun checkLogin(hasLogin: Boolean){
        checkLoginStatus = hasLogin
    }
}