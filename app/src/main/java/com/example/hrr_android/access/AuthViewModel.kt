package com.example.hrr_android.access

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrr_android.access.model.KakaoLoginResponse
import com.example.hrr_android.access.model.LoginResponse
import com.example.hrr_android.access.model.RegisterRequest
import com.example.hrr_android.access.model.RegisterResponse
import com.example.hrr_android.access.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    init {
        Log.d("HiltCheck", "AuthViewModel 주입 성공")
    }

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

    // 카카오 로그인 결과 LiveData
    private val _kakaoLoginResult = MutableLiveData<Result<KakaoLoginResponse>>()
    val kakaoLoginResult: LiveData<Result<KakaoLoginResponse>> get() = _kakaoLoginResult

    // 로그인 요청
    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val result = authRepository.login(email, password)
                _loginResult.postValue(result)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "서버 문제 발생: ${e.message}")
                _loginResult.postValue(Result.failure(e))
            }
        }
    }

    // 카카오 로그인 요청
    fun loginWithKakao(kakaoAccessToken: String) {
        viewModelScope.launch {
            val result = repository.loginWithKakao(kakaoAccessToken)

            result.onSuccess { response ->
                _kakaoLoginResult.postValue(Result.success(response))
            }.onFailure { error ->
                Log.e("KakaoLogin", "로그인 실패: ${error.message}")
                _kakaoLoginResult.postValue(Result.failure(error))
            }
        }
    }

    // 이메일 인증 코드 전송
    fun sendVerificationCode(email: String) {
        viewModelScope.launch {
            val result = authRepository.sendVerificationCode(email)
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
            val result = authRepository.confirmVerificationCode(email, code)
            result.onSuccess { id ->
                _verifiedUserId.postValue(id)  // 받은 ID 저장
            }.onFailure { e ->
                _verifiedUserId.postValue(null)
            }
        }
    }

    // 회원가입 요청
    fun registerUser(request: RegisterRequest) {
        viewModelScope.launch {
            val result = authRepository.registerUser(request)
            _registrationResult.value = result
        }
    }

    // 저장된 JWT가 있으면 자동 로그인 가능
    fun hasSavedToken(): Boolean {
        val token = authRepository.getAccessToken()
        return token != null
    }

    // API 호출 시 accessToken 값을 사용할 때
    fun getAccessToken(): String? {
        return authRepository.getAccessToken()
    }

    fun logout() {
        authRepository.clearTokens()
    }
}
