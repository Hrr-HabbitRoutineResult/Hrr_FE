package com.example.hrr_android

import android.util.Log
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userService: UserService
) {
    suspend fun loadProfile(): Result<UserResponse> {
        return try {
            val response = userService.getUserInfo()

            if (response.isSuccessful) {
                val responseBody = response.body() ?: return Result.failure(Exception("서버 응답이 비어 있습니다."))

                val userData = responseBody.success

                return if (userData != null) {
                    Result.success(userData)
                } else {
                    Result.failure(Exception("서버 응답이 올바르지 않습니다."))
                }
            } else {
                Result.failure(Exception("서버 오류 발생: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("네트워크 연결에 실패했습니다. 인터넷을 확인하세요."))
        } catch (e: Exception) {
            Result.failure(Exception("알 수 없는 오류가 발생했습니다: ${e.localizedMessage}"))
        }
    }

}