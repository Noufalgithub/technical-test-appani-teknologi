package com.example.technicaltest

import com.example.technicaltest.data.model.UserProfile
import com.example.technicaltest.data.repository.UserRepository
import com.example.technicaltest.model.User
import com.example.technicaltest.viewmodel.UsersUiState
import com.example.technicaltest.viewmodel.UsersViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class UsersViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_isIdle() {
        val fakeRepo = FakeUserRepository(shouldSucceed = true)
        val viewModel = UsersViewModel(userRepository = fakeRepo, ioDispatcher = testDispatcher)

        assertEquals(UsersUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun fetchUsers_success_emitsLoadingThenSuccess() = runTest(testDispatcher) {
        val fakeRepo = FakeUserRepository(shouldSucceed = true)
        val viewModel = UsersViewModel(userRepository = fakeRepo, ioDispatcher = testDispatcher)

        val recordedStates = mutableListOf<UsersUiState>()
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { recordedStates.add(it) }
        }

        viewModel.fetchUsers()
        advanceUntilIdle()

        // Verifikasi urutan state: Idle -> Loading -> Success
        assertEquals(3, recordedStates.size)
        assertEquals(UsersUiState.Idle, recordedStates[0])
        assertEquals(UsersUiState.Loading, recordedStates[1])
        assertTrue(recordedStates[2] is UsersUiState.Success)

        val successState = recordedStates[2] as UsersUiState.Success
        assertEquals(4, successState.users.size)
        assertEquals("Andi", successState.users.first().name)

        collectJob.cancel()
    }

    @Test
    fun fetchUsers_error_emitsLoadingThenError() = runTest(testDispatcher) {
        val fakeRepo = FakeUserRepository(shouldSucceed = false, errorMessage = "Network timeout")
        val viewModel = UsersViewModel(userRepository = fakeRepo, ioDispatcher = testDispatcher)

        val recordedStates = mutableListOf<UsersUiState>()
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { recordedStates.add(it) }
        }

        viewModel.fetchUsers()
        advanceUntilIdle()

        // Verifikasi urutan state: Idle -> Loading -> Error
        assertEquals(3, recordedStates.size)
        assertEquals(UsersUiState.Idle, recordedStates[0])
        assertEquals(UsersUiState.Loading, recordedStates[1])
        assertTrue(recordedStates[2] is UsersUiState.Error)

        val errorState = recordedStates[2] as UsersUiState.Error
        assertEquals("Network timeout", errorState.message)

        collectJob.cancel()
    }

    private class FakeUserRepository(
        private val shouldSucceed: Boolean,
        private val errorMessage: String = "Failed to fetch data"
    ) : UserRepository {
        override suspend fun getUsers(): List<User> {
            if (!shouldSucceed) throw IOException(errorMessage)
            return listOf(
                User(1, "Andi", 25, true),
                User(2, "Budi", 17, true),
                User(3, "Citra", 30, false),
                User(4, "Deni", 22, true)
            )
        }

        override suspend fun getUserById(id: Int): Result<UserProfile> {
            return Result.success(UserProfile(id, "Andi", "andi@example.com"))
        }
    }
}
