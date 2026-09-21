package com.example.technicaltest

import com.example.technicaltest.data.api.UserApiService
import com.example.technicaltest.data.model.UserResponse
import com.example.technicaltest.data.model.toDomain
import com.example.technicaltest.data.repository.UserRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()

    @Test
    fun userResponse_toDomain_mapsCorrectly() {
        val dto = UserResponse(id = 1, name = "Andi", email = "andi@example.com")
        val domain = dto.toDomain()

        assertEquals(1, domain.id)
        assertEquals("Andi", domain.name)
        assertEquals("andi@example.com", domain.email)
    }

    @Test
    fun getUserById_success_returnsMappedDomainModel() = runTest(testDispatcher) {
        val fakeApiService = object : UserApiService {
            override suspend fun getUserById(id: Int): UserResponse {
                return UserResponse(id = id, name = "Andi", email = "andi@example.com")
            }
        }

        val repository = UserRepositoryImpl(fakeApiService, testDispatcher)
        val result = repository.getUserById(1)

        assertTrue(result.isSuccess)
        val userProfile = result.getOrThrow()
        assertEquals(1, userProfile.id)
        assertEquals("Andi", userProfile.name)
        assertEquals("andi@example.com", userProfile.email)
    }

    @Test
    fun getUserById_failure_returnsResultFailure() = runTest(testDispatcher) {
        val fakeApiService = object : UserApiService {
            override suspend fun getUserById(id: Int): UserResponse {
                throw IOException("404 Not Found")
            }
        }

        val repository = UserRepositoryImpl(fakeApiService, testDispatcher)
        val result = repository.getUserById(99)

        assertTrue(result.isFailure)
        assertEquals("404 Not Found", result.exceptionOrNull()?.message)
    }
}
