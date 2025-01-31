package com.example.hrr_android.access

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hrr_android.access.model.LoginResponse
import com.example.hrr_android.access.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository(application)

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> get() = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = repository.login(email, password)
            _loginResult.postValue(result)
        }
    }

    // 저장된 JWT가 있으면 자동 로그인 가능
    fun hasSavedToken(): Boolean {
        val token = repository.getAccessToken()
        return token != null
    }

    // API 호출 시 accessToken 값을 사용할 때
    fun getAccessToken(): String? {
        return repository.getAccessToken()
    }

    fun logout() {
        repository.clearTokens()
    }
}
