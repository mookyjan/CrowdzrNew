package com.example.crowdzrnew.rest


import com.example.crowdzrnew.rest.model.SampleLoginResponse
import com.example.crowdzrnew.rest.model.SampleUserResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Addam on 7/1/2019.
 */
interface GeneralService {
    @GET("users/{username}")
    fun getUsers(@Path("username") username: String): Single<SampleUserResponse>

    @GET("login")
    fun getlogin(): Single<SampleLoginResponse>
}