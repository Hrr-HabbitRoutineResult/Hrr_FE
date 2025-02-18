package com.example.hrr_android

import com.example.hrr_android.challenge.model.ChallengeDetail
import retrofit2.Response
import retrofit2.http.GET
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
}