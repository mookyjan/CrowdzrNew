package com.example.crowdzrnew.rest.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import java.io.Serializable


/**
 * Created by Mudassir on 9/1/2019.
 */
data class SampleLoginResponse (
    @SerializedName("username")
    val username: String,

    @SerializedName("password")
    val password: String

)

data class LoginResponse(
    @SerializedName("token")
    @Expose
     val token: String? = null,
    @SerializedName("user")
    @Expose
     val user: User? = null
)

data class User (
    @SerializedName("created")
    @Expose
     val created: String? = null,
    @SerializedName("firstName")
    @Expose
     val firstName: String? = null,
    @SerializedName("lastName")
    @Expose
     val lastName: String? = null,
    @SerializedName("displayName")
    @Expose
     val displayName: String? = null,
    @SerializedName("email")
    @Expose
    val email: String? = null,
    @SerializedName("profileImageURL")
    @Expose
     val profileImageURL: String? = null,
    @SerializedName("initialsSqWeb")
    @Expose
     val initialsSqWeb: String? = null,
    @SerializedName("initialsCirWeb")
    @Expose
     val initialsCirWeb: String? = null,
    @SerializedName("initialsSqMobile")
    @Expose
     val initialsSqMobile: String? = null,
    @SerializedName("avgTimeToRespond")
    @Expose
     val avgTimeToRespond: Int? = null,
    @SerializedName("avgStarRating")
    @Expose
     val avgStarRating: Int? = null,
    @SerializedName("_id")
    @Expose
     val _id: String? = null,
    @SerializedName("mayBeInterestedShoutouts")
    @Expose
     val mayBeInterestedShoutouts: List<String>? = null,
    @SerializedName("visitedShoutouts")
    @Expose
     val visitedShoutouts: List<Any>? = null,
    @SerializedName("userDashboardData")
    @Expose
     val userDashboardData: UserDashboardData? = null
)

data class UserDashboardData (
    @SerializedName("possibleEarningsLocation")
    @Expose
    private val possibleEarningsLocation: Int? = null,
    @SerializedName("possibleEarningsInterests")
    @Expose
    private val possibleEarningsInterests: Int? = null,
    @SerializedName("activeStacksLocation")
    @Expose
    private val activeStacksLocation: Int? = null,
    @SerializedName("activeStacksInterests")
    @Expose
    private val activeStacksInterests: Int? = null,
    @SerializedName("userFeed")
    @Expose
    private val userFeed: List<Any>? = null
)