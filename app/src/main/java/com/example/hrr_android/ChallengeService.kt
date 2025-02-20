package com.example.hrr_android

import com.example.hrr_android.makechallenge.MakeChallengeRequest
import com.example.hrr_android.makechallenge.MakeChallengeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ChallengeService {
    // 오늘의 인기 챌린지 조회
    @GET("api/v1/challenge/hotness")
    suspend fun getChallengeHotness()
    : Response<ApiResponse<ChallengeHotnessResponse>>

    @POST("api/v1/challenge")
    suspend fun makeChallenge(
        @Body request: MakeChallengeRequest
    )
            : Response<ApiResponse<MakeChallengeResponse>>
}