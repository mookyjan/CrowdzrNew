package com.example.crowdzrnew.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.crowdzrnew.rest.model.User

@Database(entities = [(UserProfile::class)],version = 1,exportSchema = false)
abstract class CrowdzrDatabase :RoomDatabase() {
    abstract fun userProfileDao() : UserProfileDao
}