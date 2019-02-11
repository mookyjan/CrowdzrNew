package com.example.crowdzrnew.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Single

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM userProfile LIMIT 1")
    fun getUserProfile() : Single<UserProfile>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userProfile: UserProfile)

    @Query("DELETE FROM userProfile")
    fun clearUserProfile()

    @Query("UPDATE userProfile SET displayName = :nickname WHERE email = :email")
    fun insertDisplayname(nickname: String, email: String)

    @Query("UPDATE userProfile SET displayName = :nickname, fName = :fName WHERE email = :email")
    fun updateUserProfile(nickname: String, email: String, fName: String)

    @Query("SELECT * FROM userprofile WHERE email = :email LIMIT 1")
    fun getUserEmail(email: String): Single<UserProfile>
}