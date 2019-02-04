package com.example.crowdzrnew.rest


import com.example.crowdzrnew.rest.model.SampleLoginResponse
import com.example.crowdzrnew.rest.model.SampleUserResponse
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Addam on 7/1/2019.
 */
@Singleton
class GeneralRepository @Inject constructor(private val api: GeneralService){

    fun getUser(username: String): Single<SampleUserResponse> =
            api.getUsers(username)

    fun getLogin(): Single<SampleLoginResponse> =
            api.getlogin()
}