package com.rania.useralbum.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.asFlow
import com.rania.useralbum.db.dao.PhotoDao
import com.rania.useralbum.model.Photo
import com.rania.useralbum.network.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class PhotoRepository(private val photoDao: PhotoDao) {

    val mPhotoServe by lazy {
        UserService.createNetworkService()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertPhoto(photo: Photo) {
        photoDao.insertPhoto(photo)
    }

    fun getPhotoList(albumId: Int): Flow<Resource<List<Photo>>> {
        return networkBoundResource(
            fetchFromLocal = { photoDao.getPhotoList(albumId) },
            shouldFetchFromRemote = { it == null || it.size == 0 },
            fetchFromRemote = { mPhotoServe.getPhotoList(albumId).asFlow() },
            processRemoteResponse = { },
            saveRemoteData = { photoDao.insertAllPhotos(it) }
        ) { _, _ -> }.flowOn(Dispatchers.IO)
    }
}