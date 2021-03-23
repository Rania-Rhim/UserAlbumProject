package com.rania.useralbum.network

import androidx.lifecycle.LiveData
import com.rania.useralbum.model.Album
import com.rania.useralbum.model.Photo
import com.rania.useralbum.model.User
import com.rania.useralbum.repository.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UserAPI {

    @GET("users")
    fun getUserList(): LiveData<ApiResponse<List<User>>>

    @GET("users/1/albums?")
    fun getAlbumList(@Query("userId") userId: Int): LiveData<ApiResponse<List<Album>>>

    @GET("users/1/photos?")
    fun getPhotoList(@Query("albumId") albumId: Int): LiveData<ApiResponse<List<Photo>>>
}