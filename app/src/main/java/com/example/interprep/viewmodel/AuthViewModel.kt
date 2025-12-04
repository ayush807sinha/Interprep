package com.example.interprep.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interprep.data.repo.AuthRepository
import com.example.interprep.state.ResultState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _signUpState = MutableStateFlow<ResultState<FirebaseUser>>(ResultState.Idle)
    val signUpState: StateFlow<ResultState<FirebaseUser>> = _signUpState

    private val _signInState = MutableStateFlow<ResultState<FirebaseUser>>(ResultState.Idle)
    val signInState: StateFlow<ResultState<FirebaseUser>> = _signInState


    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _signUpState.value = ResultState.Loading
            val result = authRepository.signUp(email, password)
            _signUpState.value = result
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _signInState.value = ResultState.Loading
            try {
                val result = authRepository.signIn(email, password)
                _signInState.value = result 
            } catch (e: Exception) {
                _signInState.value = ResultState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
    }
}
