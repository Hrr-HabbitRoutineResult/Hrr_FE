package com.example.hrr_android.access.repository

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.hrr_android.access.network.AuthService
import com.example.hrr_android.access.model.EmailConfirmRequest
import com.example.hrr_android.access.model.EmailVerificationRequest
import com.example.hrr_android.access.model.KakaoLoginRequest
import com.example.hrr_android.access.model.KakaoLoginResponse
import com.example.hrr_android.access.model.LoginRequest
import com.example.hrr_android.access.model.LoginResponse
import com.example.hrr_android.access.model.RegisterRequest
import com.example.hrr_android.access.model.RegisterResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import com.google.gson.Gson

@Singleton
class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authService: AuthService
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
            Log.d("asdf", response.toString())
            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    loginResponse.success?.let { saveTokens(it.accessToken, it.refreshToken) }
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
                    saveTokens(responseBody.accessToken, responseBody.refreshToken)
                    Result.success(responseBody)
                } else {
                    Log.e("KakaoLogin", "서버 응답 오류: 응답 본문이 비어 있음")
                    Result.failure(Exception("서버 응답 오류"))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "알 수 없는 오류"
                Log.e("KakaoLogin", "로그인 실패: $errorBody")
                Result.failure(Exception("로그인 실패: $errorBody"))
            }
        } catch (e: Exception) {
            Log.e("KakaoLogin", "네트워크 오류 발생: ${e.message}", e)
            Result.failure(Exception("네트워크 오류: ${e.message}"))
        }
    }

    // EncryptedSharedPreferences를 사용하여 Token 저장
    private fun saveTokens(accessToken: String, refreshToken: String) {
        try {
            encryptedSharedPreferences.edit()
                .putString("ACCESS_TOKEN", accessToken)
                .putString("REFRESH_TOKEN", refreshToken)
                .apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 저장된 JWT 토큰을 불러오는 함수 (자동 로그인용)
    fun getAccessToken(): String? {
        return encryptedSharedPreferences.getString("ACCESS_TOKEN", null)
    }

    fun getRefreshToken(): String? {
        return encryptedSharedPreferences.getString("REFRESH_TOKEN", null)
    }

    // 로그아웃 시 JWT 삭제 (사용자가 로그아웃하면 호출)
    fun clearTokens() {
        encryptedSharedPreferences.edit()
            .remove("ACCESS_TOKEN")
            .remove("REFRESH_TOKEN")
            .apply()
    }
}
