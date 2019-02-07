package com.example.crowdzrnew.utilities

import android.content.Context
import android.content.Intent
import com.example.crowdzrnew.AppPreference
import com.example.crowdzrnew.CrowdZrApp
import com.example.crowdzrnew.R
import com.example.crowdzrnew.core.GenericErrorResponse
import com.example.crowdzrnew.core.Router
import com.example.crowdzrnew.core.bundle
import com.example.crowdzrnew.core.ui.FullScreenType
import com.example.crowdzrnew.core.ui.FullScreenUi
import com.example.crowdzrnew.database.DatabaseRepository
import com.example.crowdzrnew.feature.login.LoginActivity
import com.example.crowdzrnew.feature.status.FullScreenStatusUiActivity
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

/**
 * Created by Addam on 21/9/2018.
 */
class ErrorCodeValidator {
    companion object {
        fun checkErrorResponse(context: Context, databaseRepository: DatabaseRepository, throwable: Throwable?): Int {
            return if (throwable != null) {
                when (throwable) {
                    is HttpException -> {
                        val genericErrorResponse: GenericErrorResponse? = GenericErrorResponse.parseError(throwable.response())
                        if (genericErrorResponse != null) {
                            val status = genericErrorResponse.fault
                            if (status != null) {
                                try {
                                    if (unAuthorizedCheck(status)) {
                                        clearUserInfo(context, databaseRepository, checkErrorCode(status))
                                    } else {
                                        val errorCode = checkErrorCode(status)
                                        if (errorCode == R.string.server_err) errorScreen(FullScreenType.SERVERERROR, context) else {
                                            errorCode
                                        }
                                    }
                                } catch (e: Exception) {
                                    errorScreen(FullScreenType.SERVERERROR, context)
                                }
                            } else {
                                when (genericErrorResponse.error) {
                                    "invalid_grant" -> R.string.login_not_match
                                    else -> errorScreen(FullScreenType.SERVERERROR, context)
                                }
                            }
                        } else {
                            errorScreen(FullScreenType.SERVERERROR, context)
                        }
                    }
                    is UnknownHostException -> {
                        errorScreen(FullScreenType.NOCONNECTION, context)
                    }
                    else -> errorScreen(FullScreenType.SERVERERROR, context)
                }
            } else {
                errorScreen(FullScreenType.SERVERERROR, context)
            }
        }

        fun checkErrorCode(code: String?): Int {
            return if (code != null) {
                try {
                    when (code) {
                        "InvalidParameterException" -> R.string.InvalidParameterException
                        "UserNotFoundException" -> R.string.UserNotFoundException
                        "NotAuthorizedException" ->R.string.NotAuthorizedException
                        "500" -> R.string.server_error

                        else -> R.string.server_err
                    }
                } catch (e: Exception) {
                    R.string.server_err
                }
            } else {
                R.string.server_err
            }
        }

        fun unAuthorizedCheck(code: String?): Boolean {
            return when (code) {
                "900901" -> true
                "900902" -> true
                "2001" -> true
                "2004" -> true
                else -> false
            }
        }

        private fun clearUserInfo(context: Context, databaseRepository: DatabaseRepository, id: Int): Int {
            Completable.fromAction {

                val iPayEasyPreference = AppPreference(context)
                iPayEasyPreference.clearUserPref()
                iPayEasyPreference.setLoggedIn(false)
            }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : CompletableObserver {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onError(e: Throwable) {
                            Timber.e(e.message)
                        }

                        override fun onComplete() {
                            loginScreen(context, id)
                        }
                    })
            return R.string.empty
        }

        private fun errorScreen(screenType: FullScreenType, context: Context): Int {
            val intent = Intent(context, FullScreenStatusUiActivity::class.java)
            val fullScreenUI = FullScreenUi(screenType)
            val parameters = hashMapOf(Pair(Router.Parameter.FULL_SCREEN_UI, fullScreenUI))
            intent.putExtras(parameters.bundle)
            val applicationContext = context.applicationContext
            if (applicationContext != null && applicationContext is CrowdZrApp) {
                if (applicationContext.isOnForeground(context)) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    context.startActivity(intent)
                }
            } else {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                context.startActivity(intent)
            }
            return R.string.empty
        }

        private fun loginScreen(context: Context, id: Int) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra(Router.Parameter.SNACKBAR_MSG.name, context.getString(id))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            context.startActivity(intent)
        }
    }
}