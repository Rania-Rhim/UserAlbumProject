package com.rania.useralbum.network

import com.rania.useralbum.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UserService {

    fun createNetworkService(): UserAPI {

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()

        return retrofit.create(UserAPI::class.java)
    }
}