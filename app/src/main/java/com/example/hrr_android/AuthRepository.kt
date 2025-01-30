package com.example.hrr_android

import retrofit2.HttpException

class AuthRepository {
    private val authService: AuthService = NetworkClient.authService

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = authService.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("로그인 실패: $errorBody"))
            }
        } catch (e: HttpException) {
            Result.failure(Exception("네트워크 오류: ${e.message()}"))
        } catch (e: Exception) {
            Result.failure(Exception("알 수 없는 오류: ${e.message}"))
        }
    }
}