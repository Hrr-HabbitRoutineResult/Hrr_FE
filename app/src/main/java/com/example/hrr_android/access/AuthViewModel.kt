package com.example.hrr_android.access

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrr_android.access.model.KakaoLoginResponse
import com.example.hrr_android.access.model.LoginResponse
import com.example.hrr_android.access.model.NicknameCheckRequest
import com.example.hrr_android.access.model.NicknameCheckResponse
import com.example.hrr_android.access.model.RegisterRequest
import com.example.hrr_android.access.model.RegisterResponse
import com.example.hrr_android.access.model.TokenResponse
import com.example.hrr_android.access.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    init {
        Log.d("HiltCheck", "AuthViewModel 주입 성공")
    }

    // 로그인 결과 LiveData
    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> get() = _loginResult

    // 닉네임 중복 검사 API 응답을 저장하는 LiveData
    private val _nicknameCheckResult = MutableLiveData<Result<NicknameCheckResponse>>()
    val nicknameCheckResult: LiveData<Result<NicknameCheckResponse>> get() = _nicknameCheckResult

    // 닉네임 사용 가능 여부를 저장하는 LiveData (true: 사용 가능, false: 중복된 닉네임)
    private val _isNicknameAvailable = MutableLiveData<Boolean>()
    val isNicknameAvailable: LiveData<Boolean> get() = _isNicknameAvailable

    // 이메일 인증 코드 전송 여부 및 버튼 상태 관리
    private val _isVerified = MutableLiveData<Boolean?>()
    val isVerified: MutableLiveData<Boolean?> get() = _isVerified

    // 이메일 인증 후 받은 ID 저장
    private val _verifiedUserId = MutableLiveData<Int?>()
    val verifiedUserId: LiveData<Int?> get() = _verifiedUserId

    // 회원가입 결과 LiveData
    private val _registrationResult = MutableLiveData<Result<RegisterResponse>>()
    val registrationResult: LiveData<Result<RegisterResponse>> get() = _registrationResult

    // 카카오 로그인 결과 LiveData
    private val _kakaoLoginResult = MutableLiveData<Result<KakaoLoginResponse>>()
    val kakaoLoginResult: LiveData<Result<KakaoLoginResponse>> get() = _kakaoLoginResult

    // AuthEventManager의 로그아웃 이벤트
    val logoutEvent: LiveData<Unit>
        get() = AuthEventManager.logoutEvent

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

    // 닉네임 중복 확인 요청
    fun checkNickname(nickname: String) {
        viewModelScope.launch {
            val result = authRepository.checkNickname(NicknameCheckRequest(nickname))
            _nicknameCheckResult.postValue(result)

            result.onSuccess { response ->
                _isNicknameAvailable.postValue(response.success?.check == false)
                // success.check == false → 사용 가능
                // success.check == true → 사용 불가 (중복됨)
            }.onFailure {
                _isNicknameAvailable.postValue(false) // 오류 발생 시 기본값 설정 (사용 불가)
            }
        }
    }

    // 카카오 로그인 요청
    fun loginWithKakao(kakaoAccessToken: String) {
        viewModelScope.launch {
            val result = authRepository.loginWithKakao(kakaoAccessToken)

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
            _isVerified.postValue(null) // 네트워크 요청 전 초기화 (UI 반응 방지)

            val result = authRepository.sendVerificationCode(email)

            result.onSuccess { success ->
                if (success) {
                    _isVerified.postValue(true)  // 성공 시 true 설정
                } else {
                    _isVerified.postValue(false) // 실패 처리
                }
            }.onFailure { exception ->
                _isVerified.postValue(false) // 네트워크 오류 발생 시 false 설정
            }
        }
    }

    // 즉시 실행 방지를 위한 초기값 설정 함수 추가
    fun setIsVerified(value: Boolean?) {
        _isVerified.postValue(value)
    }

    // 이메일 인증 코드 확인 및 ID 저장
    fun confirmVerificationCode(email: String, code: String) {
        viewModelScope.launch {
            val result = authRepository.confirmVerificationCode(email, code)
            result.onSuccess { id ->
                if (id > 0) {
                    _verifiedUserId.postValue(id)
                } else {
                    _verifiedUserId.postValue(null)
                }
            }.onFailure {
                _verifiedUserId.postValue(null)
            }
        }
    }

    // 회원가입 요청
    fun registerUser(request: RegisterRequest) {
        viewModelScope.launch {
            val result = authRepository.registerUser(request)

            result.onSuccess { apiResponse ->
                apiResponse.success?.let { createdUser ->
                    tokenManager.saveTokens(createdUser.accessToken, createdUser.refreshToken)
                }
            }
            _registrationResult.value = result.mapCatching { it.success ?: throw Exception("회원가입 응답이 올바르지 않음") }
        }
    }

    // 저장된 JWT가 있으면 자동 로그인 가능
    fun hasSavedToken(): Boolean {
        val token = getAccessToken()
        return token != null
    }

    // API 호출 시 accessToken 값을 사용할 때
    private fun getAccessToken(): String? {
        return tokenManager.getAccessToken()
    }

    // 로그아웃 처리
    fun logout() {
        tokenManager.clearTokens()
    }
}
