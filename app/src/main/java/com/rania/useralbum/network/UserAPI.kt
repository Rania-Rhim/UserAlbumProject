package com.rania.useralbum.network

import com.rania.useralbum.model.Album
import com.rania.useralbum.model.Photo
import com.rania.useralbum.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UserAPI {

    @GET("users")
    fun getUserList(): Call<List<User>>

    @GET("users/1/albums?")
    fun getAlbumList(@Query("userId") userId: Int): Call<List<Album>>

    @GET("users/1/photos?")
    fun getPhotoList(@Query("albumId") albumId: Int): Call<List<Photo>>
}