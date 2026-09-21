package com.example.technicaltest.data.api

import com.example.technicaltest.data.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Soal 3: Retrofit Interface
 */
interface UserApiService {
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): UserResponse
}
