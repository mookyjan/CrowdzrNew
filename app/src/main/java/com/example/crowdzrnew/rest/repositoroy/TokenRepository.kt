package com.example.crowdzrnew.rest.repositoroy

import com.example.crowdzrnew.rest.api.TokenService
import com.example.crowdzrnew.rest.model.LoginResponse
import com.google.gson.JsonObject
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepository @Inject constructor(private val api:TokenService) {

    fun generateToken(jsonObject: JsonObject) : Single<LoginResponse>{

      return  api.generateToken(jsonObject)
    }
}