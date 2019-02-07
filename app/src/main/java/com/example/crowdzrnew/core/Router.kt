package com.example.crowdzrnew.core


import com.example.crowdzrnew.feature.MainActivity
import com.example.crowdzrnew.feature.login.LoginActivity
import com.example.crowdzrnew.feature.status.FullScreenStatusUiActivity

/**
 * Created by Mudassirkhan on 7/1/2019.
 */
class Router {
    enum class Destination {
        LOGIN,
        MAIN,
        FULL_SCREEN_UI
    }

    enum class Parameter{
        FULL_SCREEN_UI,
        USERNAME,
        PASSWORD,
        SNACKBAR_MSG
    }

    companion object {
        fun getClass(destination: Destination): Class<*> {
            return when (destination) {
                Destination.LOGIN -> LoginActivity::class.java
                Destination.MAIN -> MainActivity::class.java
                Destination.FULL_SCREEN_UI -> FullScreenStatusUiActivity::class.java
                else -> {
                    TODO("Implement Default case")
                }
            }
        }
    }
}