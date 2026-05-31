package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object CodeSent : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private var currentPhone = ""

    fun sendOtp(phone: String) {
        _authState.value = AuthState.Loading
        currentPhone = phone
        
        viewModelScope.launch {
            // Simulated network delay
            delay(1500)
            _authState.value = AuthState.CodeSent
        }
    }

    fun verifyOtp(code: String) {
        _authState.value = AuthState.Loading
        
        viewModelScope.launch {
            // Simulated network delay
            delay(1500)
            if (code == "1234") {
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error("Invalid Demo OTP. Use 1234.")
            }
        }
    }
    
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
