package com.example.crowdzrnew.rest.typeAdapter

import com.example.crowdzrnew.core.Status
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * Created by Addam on 21/9/2018.
 */
class StatusTypeAdapter: JsonDeserializer<Status>{
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Status {
        val jsonObject = json!!.asJsonObject


        return Status(jsonObject.get("correlationID").asString, jsonObject.get("code").asString, jsonObject.get("message").asString)

    }

}