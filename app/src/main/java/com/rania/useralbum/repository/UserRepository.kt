package com.rania.useralbum.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.asFlow
import com.rania.useralbum.db.dao.UserDao
import com.rania.useralbum.model.User
import com.rania.useralbum.network.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class UserRepository(private val userDao: UserDao) {

    val mUserServe by lazy {
        UserService.createNetworkService()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    fun getUserList(): Flow<Resource<List<User>>> {
        return networkBoundResource(
            fetchFromLocal = { userDao.getUserList() },
            shouldFetchFromRemote = { it == null || it.size == 0 },
            fetchFromRemote = { mUserServe.getUserList().asFlow() },
            processRemoteResponse = { },
            saveRemoteData = { userDao.insertAllUsers(it) }
        ) { _, _ -> }.flowOn(Dispatchers.IO)
    }
}