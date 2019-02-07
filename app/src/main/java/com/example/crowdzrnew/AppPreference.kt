package com.example.crowdzrnew

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.crowdzrnew.database.User
import com.example.crowdzrnew.database.UserProfile
import javax.inject.Inject

/**
 * Created by Addam on 7/1/2019.
 */
class AppPreference() {
    companion object {
        const val FIRST_RUN = "firstRun"
        const val IS_LOGGED_IN = "isLogin"
        const val ACCESS_TOKEN = "access_token"
        const val EMAIL="email"
    }

    private lateinit var prefs: SharedPreferences

    @Inject
    constructor(context: Context): this(){
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun isFirstRun() : Boolean {
        return prefs.getBoolean(FIRST_RUN, true)
    }

    fun setFirstRun(hasRun: Boolean = false) {
        val edit = prefs.edit()
        edit.putBoolean(FIRST_RUN, hasRun)
        edit.apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(IS_LOGGED_IN, false)
    }

    fun setLoggedIn(isLoggedIn: Boolean = false) {
        val edit =  prefs.edit()
        edit.putBoolean(IS_LOGGED_IN, isLoggedIn)
        edit.apply()
    }

    fun setAccessToken(accessToken: String) {
        prefs.edit().putString(ACCESS_TOKEN, accessToken).apply()
    }

    fun getAccessToken(): String {
        return prefs.getString(ACCESS_TOKEN, "")
    }

    fun setUserPref(user: User) {
        val edit = prefs.edit()
        edit.putString(ACCESS_TOKEN, user.access_token)
        edit.putString(EMAIL, user.email)
        edit.apply()
    }

    fun clearUserPref() {
        val edit = prefs.edit()
        edit.putString(ACCESS_TOKEN, "")
        edit.putString(EMAIL, "")
        edit.apply()
    }

    fun getUserPref(): User {
        return User(1,prefs.getString(EMAIL,""),
            prefs.getString(ACCESS_TOKEN, ""))
    }
}