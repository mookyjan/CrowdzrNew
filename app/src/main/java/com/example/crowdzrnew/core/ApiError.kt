package com.example.crowdzrnew.core

import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import retrofit2.Response
import java.io.IOException

data class ApiError(val status: Int, val message: String)

data class GenericErrorResponse(
        @SerializedName("message")
        @Expose
        val error_description: String? = null,
        @SerializedName("name")
        @Expose
        val error: String? = null,
        @SerializedName("code", alternate = ["fault"])
        var fault: String? = null) {

    companion object {
        fun parseError(response: Response<*>): GenericErrorResponse {
            return try {
                val type = object : TypeToken<GenericErrorResponse>() {}.type
                Gson().fromJson(response.errorBody()?.string(), type)
            } catch (e: IOException) {
                Timber.e { e.message.toString() }
                GenericErrorResponse()
            } catch (e: IllegalStateException) {
                Timber.e { e.message.toString() }
                GenericErrorResponse()
            } catch (e: JsonSyntaxException) {
                Timber.e { e.message.toString() }
                GenericErrorResponse()
            }
        }
    }
}