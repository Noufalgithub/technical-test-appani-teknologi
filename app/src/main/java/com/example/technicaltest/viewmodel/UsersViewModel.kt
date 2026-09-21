package com.example.technicaltest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.technicaltest.data.repository.UserRepository
import com.example.technicaltest.model.User
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Soal 2: UI State
 */
sealed interface UsersUiState {
    data object Idle : UsersUiState
    data object Loading : UsersUiState
    data class Success(val users: List<User>) : UsersUiState
    data class Error(val message: String, val cause: Throwable? = null) : UsersUiState
}

/**
 * Soal 2: ViewModel dengan Coroutines dan StateFlow
 */
class UsersViewModel(
    private val userRepository: UserRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _uiState = MutableStateFlow<UsersUiState>(UsersUiState.Idle)
    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()

    fun fetchUsers() {
        viewModelScope.launch {
            _uiState.value = UsersUiState.Loading
            try {
                val result = withContext(ioDispatcher) {
                    userRepository.getUsers()
                }
                _uiState.value = UsersUiState.Success(result)
            } catch (e: CancellationException) {
                // Structured concurrency: Jangan swallow CancellationException!
                throw e
            } catch (e: Throwable) {
                _uiState.value = UsersUiState.Error(
                    message = e.localizedMessage ?: "Terjadi kesalahan saat memuat data.",
                    cause = e
                )
            }
        }
    }
}
