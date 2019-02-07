package com.example.crowdzrnew.rest.api

import com.example.crowdzrnew.rest.model.LoginResponse
import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.http.*

interface TokenService {
    @POST("signin")
    fun generateToken(@Body jsonObject: JsonObject): Single<LoginResponse>
}