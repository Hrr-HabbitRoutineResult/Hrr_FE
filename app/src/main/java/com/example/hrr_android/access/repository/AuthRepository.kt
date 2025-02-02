package com.example.hrr_android.access.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.hrr_android.access.network.AuthService
import com.example.hrr_android.NetworkClient
import com.example.hrr_android.access.model.EmailConfirmRequest
import com.example.hrr_android.access.model.EmailVerificationRequest
import com.example.hrr_android.access.model.LoginRequest
import com.example.hrr_android.access.model.LoginResponse

class AuthRepository(context: Context) {
    private val authService: AuthService = NetworkClient.authService

    // SharedPreferences 사용하여 JWT 저장
    private val sharedPreferences: SharedPreferences =
        context.applicationContext.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)

    // 로그인 요청
    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = authService.login(LoginRequest(email, password))

            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    saveTokens(loginResponse.accessToken, loginResponse.refreshToken)
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

    // JWT & Refresh Token을 SharedPreferences에 저장하는 함수
    private fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPreferences.edit()
            .putString("ACCESS_TOKEN", accessToken)
            .putString("REFRESH_TOKEN", refreshToken)
            .apply()
    }

    // 저장된 JWT 토큰을 불러오는 함수 (자동 로그인할 때 사용)
    fun getAccessToken(): String? {
        return sharedPreferences.getString("ACCESS_TOKEN", null)
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString("REFRESH_TOKEN", null)
    }

    // 로그아웃 시 JWT 삭제 (사용자가 로그아웃하면 호출)
    fun clearTokens() {
        sharedPreferences.edit().clear().apply()
    }
}
