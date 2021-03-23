package com.rania.useralbum.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.asFlow
import com.rania.useralbum.db.dao.AlbumDao
import com.rania.useralbum.model.Album
import com.rania.useralbum.network.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class AlbumRepository(private val albumDao: AlbumDao) {

    val mAlbumServe by lazy {
        UserService.createNetworkService()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertAlbum(album: Album) {
        albumDao.insertAlbum(album)
    }

    fun getAlbumList(userId: Int): Flow<Resource<List<Album>>> {
        return networkBoundResource(
            fetchFromLocal = { albumDao.getAlbumList(userId) },
            shouldFetchFromRemote = { it == null || it.size == 0 },
            fetchFromRemote = { mAlbumServe.getAlbumList(userId).asFlow() },
            processRemoteResponse = { },
            saveRemoteData = { albumDao.insertAllAlbums(it) }
        ) { _, _ -> }.flowOn(Dispatchers.IO)
    }
}