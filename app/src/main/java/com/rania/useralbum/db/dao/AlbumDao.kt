package com.rania.useralbum.db.dao

import androidx.room.*
import com.rania.useralbum.model.Album
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

    @Query("SELECT * FROM album_table WHERE userId=:userId ORDER BY id")
    fun getAlbumList(userId: Int): Flow<List<Album>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlbum(album: Album)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllAlbums(albumList: List<Album>)

    @Delete
    suspend fun deleteAlbum(album: Album)

    @Update
    suspend fun updateAlbum(album: Album)

}