package com.rania.useralbum

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.rania.useralbum.db.UserAlbumDataBase
import com.rania.useralbum.repository.AlbumRepository
import com.rania.useralbum.repository.UserRepository

class UserAlbumApplication : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val mUserAlbumDatabase by lazy { UserAlbumDataBase.getDatabase(this) }

    val mUserRepository by lazy { UserRepository(mUserAlbumDatabase.userDao()) }
    val mAlbumRepository by lazy { AlbumRepository(mUserAlbumDatabase.albumDao()) }

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }
}