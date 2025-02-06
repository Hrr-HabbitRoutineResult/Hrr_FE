package com.example.hrr_android

import java.io.IOException
import javax.inject.Inject

class UserRepository @Inject constructor(networkClient: NetworkClient) {

    private val userService = networkClient.userService

    suspend fun loadProfile(): Result<UserResponse> {
        return try {
            val response = userService.getUserInfo()

            // HTTP 상태 코드 처리
            when {
                response.isSuccessful -> {
                    val responseBody = response.body()
                    if (responseBody?.success?.data != null) {
                        Result.success(responseBody.success.data)
                    } else {
                        Result.failure(Exception("서버 응답이 비어 있습니다."))
                    }
                }
                response.code() == 400 -> {
                    Result.failure(Exception("잘못된 요청입니다. (400)"))
                }
                response.code() == 500 -> {
                    Result.failure(Exception("서버 내부 오류가 발생했습니다. (500)"))
                }
                else -> {
                    Result.failure(Exception("알 수 없는 오류가 발생했습니다. (${response.code()})"))
                }
            }
        } catch (e: IOException) {
            // 네트워크 연결 오류 (인터넷 없음 등)
            Result.failure(Exception("네트워크 연결에 실패했습니다. 인터넷을 확인하세요."))
        } catch (e: Exception) {
            // 기타 예상치 못한 예외 처리
            Result.failure(Exception("알 수 없는 오류가 발생했습니다: ${e.localizedMessage}"))
        }
    }


}