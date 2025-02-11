package com.example.hrr_android

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    // 사용자 기본 정보 조회
    @GET("/api/v1/users/{userId}")
    suspend fun getUserInfo(@Path("userId") userId: Int): Response<ApiResponse<UserResponse>>

    // 챌린지 기록 조회
    @GET("/api/v1/users/challenges/verification/history")
    suspend fun getChallengeHistory(): Response<ApiResponse<List<HistoryResponse>>>
}