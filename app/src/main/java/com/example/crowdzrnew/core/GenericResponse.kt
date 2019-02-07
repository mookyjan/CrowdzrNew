package com.example.crowdzrnew.core

import com.example.crowdzrnew.rest.typeAdapter.StatusTypeAdapter
import com.google.gson.annotations.Expose
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName


/**
 * Created by Addam on 3/7/2018.
 */
data class GenericResponse(
        @JsonAdapter(StatusTypeAdapter::class)
        @SerializedName("code")
        @Expose
        val status: Status,
        @SerializedName("message")
        @Expose
        val error_description: String,
        @SerializedName("name")
        @Expose
        val error: String
)

data class Status(
        @SerializedName("name")
        @Expose
        val correlationID: String? = null,

        @SerializedName("code")
        @Expose
        val code: String?,

        @SerializedName("message")
        @Expose
        val message: String
)