package com.example.hrr_android

import com.example.hrr_android.challenge.model.ChallengeDetail
import com.example.hrr_android.challenge.model.PhotoUploadResponse
import com.example.hrr_android.challenge.model.PostResponse
import com.example.hrr_android.challenge.model.VerificationRequest
import com.example.hrr_android.challenge.model.VerificationUploadResponse
import com.example.hrr_android.challenge.model.WeeklyVerificationResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    // 사진 업로드
    @Multipart
    @POST("/api/v1/utils/upload")
    suspend fun uploadPhoto(
        @Part image: MultipartBody.Part
    ): Response<ApiResponse<PhotoUploadResponse>>
    // 사진 인증 글 작성
    @POST("/api/v1/verification/{challengeId}/verification/camera")
    suspend fun uploadVerification(
        @Path("challengeId") challengeId: Int,
        @Body request: VerificationRequest
    ): Response<ApiResponse<VerificationUploadResponse>>

    // 인증 상세 조회
    @GET("/api/v1/verification/{verificationId}")
    suspend fun getVerificationDetail(
        @Path("verificationId") verificationId: Int
    ): Response<ApiResponse<PostResponse>>
}