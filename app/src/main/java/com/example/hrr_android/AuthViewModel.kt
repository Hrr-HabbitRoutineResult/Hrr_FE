package com.example.hrr_android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()

    val loginResult: LiveData<Result<LoginResponse>> get() = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch { // ✅ Coroutine 사용
            val result = repository.login(email, password)
            _loginResult.postValue(result)
        }
    }
}