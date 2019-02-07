package com.example.crowdzrnew.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "userprofile")
data class UserProfile (@PrimaryKey var email: String = "",
                        var lastName: String? = "",
                        var fName: String? = "",
                        var displayName: String? = "",
                        var profileImageUrl: String? = "",
                        var accessToken: String?="",
                        var password: String? = "")