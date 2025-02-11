package com.example.hrr_android.access.repository

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.hrr_android.access.TokenManager
import com.example.hrr_android.access.network.AuthService
import com.example.hrr_android.access.model.EmailConfirmRequest
import com.example.hrr_android.access.model.EmailVerificationRequest
import com.example.hrr_android.access.model.KakaoLoginRequest
import com.example.hrr_android.access.model.KakaoLoginResponse
import com.example.hrr_android.access.model.LoginRequest
import com.example.hrr_android.access.model.LoginResponse
import com.example.hrr_android.access.model.RegisterRequest
import com.example.hrr_android.access.model.RegisterResponse
import com.example.hrr_android.access.model.TokenRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authService: AuthService,
    private val tokenManager: TokenManager  // TokenManager를 주입받음
) {
    // EncryptedSharedPreferences 설정
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedSharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    // 로그인 요청
    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = authService.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    // 로그인 성공 시 TokenManager를 이용하여 토큰 저장
                    loginResponse.success?.let {
                        tokenManager.saveTokens(it.accessToken, it.refreshToken)
                    }
                    Result.success(loginResponse)
                } else {
                    Result.failure(Exception("로그인 실패: 서버 응답 본문이 null"))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("로그인 실패: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("네트워크 오류: ${e.message}"))
        }
    }

    // 이메일 인증 코드 전송
    suspend fun sendVerificationCode(email: String): Result<String> {
        return try {
            val response = authService.sendVerificationCode(EmailVerificationRequest(email))
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    Result.success("이메일 인증 코드가 전송되었습니다.")
                } else {
                    Result.failure(Exception("서버 응답이 올바르지 않습니다."))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("이메일 인증 코드 전송 실패: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("네트워크 오류: ${e.message}"))
        }
    }

    // 이메일 인증 코드 확인
    suspend fun confirmVerificationCode(email: String, code: String): Result<Int> {
        return try {
            val response = authService.confirmVerificationCode(EmailConfirmRequest(email, code))

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.verified == true) {
                    Result.success(responseBody.id)
                } else {
                    Result.failure(Exception("이메일 인증 실패: 코드가 올바르지 않음"))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("이메일 인증 실패: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("네트워크 오류: ${e.message}"))
        }
    }

    // 회원가입 요청
    suspend fun registerUser(request: RegisterRequest): Result<RegisterResponse> {
        return try {
            val response = authService.registerUser(request)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    Result.success(responseBody)
                } else {
                    Result.failure(Exception("응답 본문이 비어있음"))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("회원가입 실패: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("네트워크 오류: ${e.message}"))
        }
    }

    // 카카오 로그인 요청
    suspend fun loginWithKakao(kakaoAccessToken: String): Result<KakaoLoginResponse> {
        return try {
            val request = KakaoLoginRequest(kakaoAccessToken)
            val response = authService.loginWithKakao(request)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    // TokenManager를 이용하여 토큰 저장
                    tokenManager.saveTokens(responseBody.accessToken, responseBody.refreshToken)
                    Result.success(responseBody)
                } else {
                    Result.failure(Exception("서버 응답 오류: 응답 본문이 비어 있음"))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "알 수 없는 오류"
                Result.failure(Exception("로그인 실패: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("네트워크 오류: ${e.message}"))
        }
    }

    // 토큰 갱신 요청
    suspend fun refreshAccessToken(): String? {
        val refreshToken = tokenManager.getRefreshToken() ?: return null

        return try {
            val result = authService.refreshToken(TokenRequest(refreshToken))

            if (result.isSuccessful) {
                result.body()?.success?.accessToken?.also { newToken ->
                    tokenManager.saveAccessToken(newToken)
                }
            } else {
                Log.e("AuthRepository", "액세스 토큰 갱신 실패 - 응답 코드: ${result.code()}")
                Log.e("AuthRepository", "액세스 토큰 갱신 실패 - 응답 바디: ${result.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "토큰 갱신 중 예외 발생: ${e.message}")
            null
        }
    }
}
