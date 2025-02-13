package com.example.hrr_android

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
    // 사용자 기본 정보 조회
    @GET("/api/v1/users/{userId}")
    suspend fun getUserInfo(@Path("userId") userId: Int): Response<ApiResponse<UserResponse>>

    // 챌린지 기록 조회
    @GET("/api/v1/users/challenges/verification/history")
    suspend fun getChallengeHistory(): Response<ApiResponse<List<HistoryResponse>>>

    // 참가 중인 챌린지 조회
    @GET("/api/v1/users/{userId}/challenges/ongoing")
    suspend fun getChallengesOngoing(
        @Path("userId") userId: Int
    ): Response<ApiResponse<ChallengesOngoingResponse>>

    // 참가 중인 챌린지 조회
    @GET("/api/v1/users/{userId}/challenges/completed")
    suspend fun getChallengesEnd(
        @Path("userId") userId: Int
    ): Response<ApiResponse<ChallengeEndResponse>>

    // 팔로워 리스트 조회
    @GET("/api/v1/users/{userId}/follower")
    suspend fun getFollowers(
        @Path("userId") userId: Int
    ): Response<ApiResponse<FollowResponse>>

    // 팔로잉 리스트 조회
    @GET("/api/v1/users/{userId}/following")
    suspend fun getFollowings(
        @Path("userId") userId: Int
    ): Response<ApiResponse<FollowResponse>>

    // 사용자 팔로우 처리
    @POST("/api/v1/users/{followedUserId}/follow")
    suspend fun follow(
        @Path("followedUserId") followedUserId: Int
    ): Response<ApiResponse<FollowResult>>

    // 사용자 언팔로우 처리
    @DELETE("/api/v1/users/{unfollowedUserId}/unfollow")
    suspend fun unFollow(
        @Path("unfollowedUserId") unfollowedUserId: Int
    ): Response<ApiResponse<FollowResult>>

}