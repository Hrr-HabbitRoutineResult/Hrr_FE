package com.example.hrr_android

import retrofit2.Response
import retrofit2.http.GET

interface ChallengeService {
    // 오늘의 인기 챌린지 조회
    @GET("/api/v1/challenge/hotness")
    suspend fun getChallengeHotness()
    : Response<ApiResponse<ChallengeHotnessResponse>>
}