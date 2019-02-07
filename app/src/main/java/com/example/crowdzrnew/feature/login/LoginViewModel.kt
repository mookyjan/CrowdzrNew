package com.example.crowdzrnew.feature.login


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import com.example.crowdzrnew.AppPreference
import com.example.crowdzrnew.AppResourceProvider
import com.example.crowdzrnew.R
import com.example.crowdzrnew.core.GenericErrorResponse
import com.example.crowdzrnew.core.Router
import com.example.crowdzrnew.core.event.SnackbarMessage
import com.example.crowdzrnew.core.event.SnackbarMessageEvent
import com.example.crowdzrnew.core.event.StartActivityEvent
import com.example.crowdzrnew.core.event.StartActivityModel
import com.example.crowdzrnew.core.ui.FullScreenType
import com.example.crowdzrnew.core.ui.FullScreenUi
import com.example.crowdzrnew.core.util.SchedulerProvider
import com.example.crowdzrnew.database.DatabaseRepository
import com.example.crowdzrnew.database.User
import com.example.crowdzrnew.database.UserProfile
import com.example.crowdzrnew.rest.GeneralRepository
import com.example.crowdzrnew.rest.model.LoginResponse
import com.example.crowdzrnew.rest.model.SampleLoginResponse
import com.example.crowdzrnew.rest.repositoroy.TokenRepository
import com.example.crowdzrnew.utilities.ErrorCodeValidator
import com.example.crowdzrnew.utilities.ObservableString
import com.example.crowdzrnew.utilities.Validator
import com.example.crowdzrnew.utilities.observe
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.i
import com.google.gson.JsonObject
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.view.*
import retrofit2.HttpException

class LoginViewModel(private val schedulerProvider: SchedulerProvider,
                     private val tokenRepository: TokenRepository,
                     private val databaseRepository: DatabaseRepository,
                     private val appResourceProvider: AppResourceProvider,
                     private val appPreference: AppPreference):ViewModel() {

    var username =ObservableString("")
    var password = ObservableString("")
    var isFormValid = ObservableBoolean(false)
    lateinit var userInfo :User
    val loading = MutableLiveData<Boolean>()
    private val userIdValid = ObservableBoolean(false)
    private val passwordValid = ObservableBoolean(false)
    private val token = ObservableString("")
    val snackbarMessage: SnackbarMessage = SnackbarMessage()
    val tokenEvent: SnackbarMessageEvent= SnackbarMessageEvent()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
     val startActivityEvent: StartActivityEvent = StartActivityEvent()
    lateinit var userLoginListener: UserLoginListener

    init {
        username.observe().map {
            Validator.ReasonValidator.validateEmail(it)
        }.subscribe { userIdValid.set(it) }.addTo(compositeDisposable)

        password.observe().map {
            Validator.ReasonValidator.validate(it)
        }.subscribe{ passwordValid.set(it)}.addTo(compositeDisposable)

        io.reactivex.Observable.combineLatest(userIdValid.observe(), passwordValid.observe(),
            BiFunction { a: Boolean, b: Boolean -> a && b })
            .subscribe { isFormValid.set(it) }.addTo(compositeDisposable)
    }

    private fun callLoginApi():Single<LoginResponse>{

        val jsonObject = JsonObject()
        jsonObject.addProperty("username", username.get().toString())
        jsonObject.addProperty("password", password.get().toString())

        return tokenRepository.generateToken(jsonObject)
            .compose(schedulerProvider.getSchedulersForSingle())

    }

    fun onLoginClicked(){
        loading.postValue(true)
        callLoginApi().subscribeBy (onSuccess = {
            Timber.d{"sucess response for Login $it"}
            var user=it.user
            userInfo= User(1,
                user!!.email!!,
                it.token)
            tokenEvent.value=it.token.toString()
           saveUserInfo()
            loading.postValue(false)
//            startActivityEvent.value= StartActivityModel(Router.Destination.MAIN,
//                hashMapOf(Pair(Router.Parameter.USERNAME,it.token)))

        },onError = {
            loading.postValue(false)
            Timber.e { "Failed Response For Login "+it.message.toString() }
            if (it is HttpException){
                val errorResponse = GenericErrorResponse.parseError(it.response())
                val status =errorResponse.fault
                if (status!=null){
                    when(status){
                        "InvalidParameterException"->{
                            userLoginListener.onFieldError(R.id.et_email)
                            userLoginListener.onFieldError(R.id.et_password,R.string.login_not_match)
                        }else ->parseError(status)

                    }
                }else{
                    if (errorResponse.error!=null && errorResponse.error.equals("NotAuthorizedException",ignoreCase = true)){
                        userLoginListener.onFieldError(R.id.et_email)
                        userLoginListener.onFieldError(R.id.et_password,R.string.login_not_match)
                    }else{
                        val fullScreenUI= FullScreenUi(FullScreenType.SERVERERROR)
                        startActivityEvent.value=StartActivityModel(Router.Destination.FULL_SCREEN_UI, hashMapOf(Pair(Router.Parameter.FULL_SCREEN_UI, fullScreenUI)))

                    }
                }
            }else{
                parseError(it)
            }
        })
    }

    private fun parseError(errorCode: String?) {
        val errorMsg = ErrorCodeValidator.checkErrorCode(errorCode)
        if (errorMsg == R.string.server_err) {
            val fullScreenUI = FullScreenUi(FullScreenType.SERVERERROR)
            startActivityEvent.value = StartActivityModel(Router.Destination.FULL_SCREEN_UI, hashMapOf(Pair(Router.Parameter.FULL_SCREEN_UI, fullScreenUI)))
        } else {
            snackbarMessage.postValue(appResourceProvider.getString(errorMsg))
        }
    }

    private fun parseError(it: Throwable) {
        snackbarMessage.postValue(appResourceProvider.getString(ErrorCodeValidator.checkErrorResponse(appResourceProvider.context, databaseRepository, it)))
    }
    fun saveUserInfo(){
        appPreference.setUserPref(userInfo)
        appPreference.setLoggedIn(true)
        token.set(userInfo.access_token)
        saveUserProfileToDB(userProfile = UserProfile(userInfo.email!!,
            "","",username.get().toString(),"",userInfo.access_token,password.get().toString()))
    }

    private fun saveUserProfileToDB(userProfile:UserProfile){

        Completable.fromAction{
            databaseRepository.insertUserProfile(userProfile)
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver{
                override fun onComplete() {
                    startActivityEvent.value= StartActivityModel(Router.Destination.MAIN,
                hashMapOf(Pair(Router.Parameter.USERNAME,userProfile.displayName)))
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    Timber.e { e.message!! }
                }

            })
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    interface UserLoginListener {
        fun onFieldError(id: Int, msg: Int = 0)
    }
}