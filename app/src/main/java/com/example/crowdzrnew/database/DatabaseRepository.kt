package com.example.crowdzrnew.database

import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseRepository @Inject constructor(private val userProfileDao: UserProfileDao) {

    fun getUserProfile():Single<UserProfile> = userProfileDao.getUserProfile()

    fun insertUserProfile(userProfile: UserProfile) = userProfileDao.insert(userProfile)

}