package com.rania.useralbum.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rania.useralbum.db.dao.AlbumDao
import com.rania.useralbum.db.dao.UserDao
import com.rania.useralbum.model.Album
import com.rania.useralbum.model.Photo
import com.rania.useralbum.model.User
import com.rania.useralbum.utils.Constants

@Database(
    entities = arrayOf(User::class, Album::class, Photo::class),
    version = 1,
    exportSchema = false
)
abstract class UserAlbumDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun albumDao(): AlbumDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time
        @Volatile
        private var INSTANCE: UserAlbumDataBase? = null

        fun getDatabase(context: Context): UserAlbumDataBase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserAlbumDataBase::class.java,
                    Constants.USER_ALBUM_DATABASE
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}