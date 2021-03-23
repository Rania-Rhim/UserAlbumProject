package com.rania.useralbum.db.dao

import androidx.room.*
import com.rania.useralbum.model.Photo
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Query("SELECT * FROM photo_table WHERE albumId=:albumId ORDER BY id")
    fun getPhotoList(albumId: Int): Flow<List<Photo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(photo: Photo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllPhotos(photoList: List<Photo>)

    @Delete
    suspend fun deletePhoto(photo: Photo)

    @Update
    suspend fun updatePhoto(photo: Photo)

}