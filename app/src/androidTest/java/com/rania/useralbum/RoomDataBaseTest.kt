package com.rania.useralbum

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rania.useralbum.db.UserAlbumDataBase
import com.rania.useralbum.db.dao.UserDao
import com.rania.useralbum.model.User
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoomDataBaseTest {

    private lateinit var userDao: UserDao
    private lateinit var db: UserAlbumDataBase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, UserAlbumDataBase::class.java)
            .allowMainThreadQueries()
            .build()
        db = Room.inMemoryDatabaseBuilder(
            context, UserAlbumDataBase::class.java
        ).build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun review() {
        val user =
            User(id = 1, "John Doe", "j_doe", "john.doe@gmail.com", "111222333", "johndoe.org")

        userDao.insertUser(user)
        val userFromDb = userDao.getUserList()


        Assert.assertNotNull(userFromDb)
    }

}