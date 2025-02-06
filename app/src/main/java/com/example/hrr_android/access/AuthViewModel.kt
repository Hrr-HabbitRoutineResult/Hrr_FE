package com.example.hrr_android.access

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hrr_android.access.model.LoginResponse
import com.example.hrr_android.access.model.RegisterRequest
import com.example.hrr_android.access.model.RegisterResponse
import com.example.hrr_android.access.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository(application)

    // 로그인 결과 LiveData
    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> get() = _loginResult

    // 이메일 인증 코드 전송 여부 및 버튼 상태 관리
    private val _isVerified = MutableLiveData<Boolean>()
    val isVerified: LiveData<Boolean> get() = _isVerified

    // 이메일 인증 후 받은 ID 저장
    private val _verifiedUserId = MutableLiveData<Int?>()
    val verifiedUserId: LiveData<Int?> get() = _verifiedUserId

    // 회원가입 결과 LiveData
    private val _registrationResult = MutableLiveData<Result<RegisterResponse>>()
    val registrationResult: LiveData<Result<RegisterResponse>> get() = _registrationResult

    // 로그인 요청
    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = repository.login(email, password)
            _loginResult.postValue(result)
        }
    }

    // 이메일 인증 코드 전송
    fun sendVerificationCode(email: String) {
        viewModelScope.launch {
            val result = repository.sendVerificationCode(email)
            result.onSuccess {
                _isVerified.postValue(true)  // 성공 시 true 저장
            }.onFailure {
                _isVerified.postValue(false)  // 실패 시 false 저장
            }
        }
    }

    // 이메일 인증 코드 확인 및 ID 저장
    fun confirmVerificationCode(email: String, code: String) {
        viewModelScope.launch {
            val result = repository.confirmVerificationCode(email, code)
            result.onSuccess { id ->
                _verifiedUserId.postValue(id)  // 받은 ID 저장
                Log.d("AuthID", "이메일 인증 성공 - 받은 ID: $id")
            }.onFailure { e ->
                _verifiedUserId.postValue(null)
                Log.e("AuthID", "이메일 인증 실패: ${e.message}")
            }
        }
    }

    // 회원가입 요청
    fun registerUser(request: RegisterRequest) {
        viewModelScope.launch {
            val result = repository.registerUser(request)
            _registrationResult.value = result
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
