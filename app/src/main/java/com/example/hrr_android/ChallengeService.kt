package com.example.hrr_android

import com.example.hrr_android.challenge.model.ChallengeDetail
import com.example.hrr_android.challenge.model.WeeklyVerificationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChallengeService {
    // 오늘의 인기 챌린지 조회
    @GET("api/v1/challenge/hotness")
    suspend fun getChallengeHotness()
    : Response<ApiResponse<ChallengeHotnessResponse>>

    // 챌린지 상세 정보 조회
    @GET("/api/v1/challenge/{challengeId}")
    suspend fun getChallengeDetail(
        @Path("challengeId") challengeId: Int
    ): Response<ApiResponse<ChallengeDetail>>

    // 사용자 프로필 정보 조회
    @GET("/api/v1/users/{userId}")
    suspend fun getUserProfile(
        @Path("userId") userId: Int
    ): Response<ApiResponse<UserResponse>>

    // 챌린지 참가
    @POST("/api/v1/challenge/{challengeId}/join")
    suspend fun joinChallenge(
        @Path("challengeId") challengeId: Int
    ): Response<ApiResponse<ChallengeDetail>>

    // 이번 주 인증 현황 조회
    @GET("/api/v1/verification/{challengeId}/verification/weekly")
    suspend fun getWeeklyVerification(
        @Path("challengeId") challengeId: Int
    ): Response<ApiResponse<WeeklyVerificationResponse>>
}