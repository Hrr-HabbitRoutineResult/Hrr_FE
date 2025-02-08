package com.example.hrr_android

import retrofit2.Response
import retrofit2.http.GET

interface UserService {
    // 사용자 기본 정보 조회
    @GET("/api/v1/users/me")
    suspend fun getUserInfo(): Response<ApiResponse<UserResponse>>
}