package com.example.crowdzrnew.core


import com.example.crowdzrnew.feature.MainActivity
import com.example.crowdzrnew.feature.login.LoginActivity

/**
 * Created by Mudassirkhan on 7/1/2019.
 */
class Router {
    enum class Destination {
        LOGIN,
        MAIN
    }

    enum class Parameter{
        USERNAME,
        PASSWORD
    }

    companion object {
        fun getClass(destination: Destination): Class<*> {
            return when (destination) {
                Destination.LOGIN -> LoginActivity::class.java
                Destination.MAIN -> MainActivity::class.java
                else -> {
                    TODO("Implement Default case")
                }
            }
        }
    }
}