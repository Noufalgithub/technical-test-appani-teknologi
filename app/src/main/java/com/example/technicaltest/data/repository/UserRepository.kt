package com.example.technicaltest.data.repository

import com.example.technicaltest.data.api.UserApiService
import com.example.technicaltest.data.model.UserProfile
import com.example.technicaltest.data.model.toDomain
import com.example.technicaltest.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository interface untuk menyediakan data user baik ke ViewModel (Soal 2) maupun by ID (Soal 3)
 */
interface UserRepository {
    suspend fun getUsers(): List<User>
    suspend fun getUserById(id: Int): Result<UserProfile>
}

class UserRepositoryImpl(
    private val apiService: UserApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {

    override suspend fun getUsers(): List<User> = withContext(ioDispatcher) {
        // Contoh implementasi getUsers jika memanggil API
        listOf(
            User(1, "Andi", 25, true),
            User(2, "Budi", 17, true),
            User(3, "Citra", 30, false),
            User(4, "Deni", 22, true)
        )
    }

    override suspend fun getUserById(id: Int): Result<UserProfile> = withContext(ioDispatcher) {
        runCatching {
            apiService.getUserById(id).toDomain()
        }
    }
}
