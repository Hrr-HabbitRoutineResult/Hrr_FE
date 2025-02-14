package com.example.hrr_android

import com.google.gson.annotations.SerializedName

data class ChallengeHotnessResponse(
    @SerializedName("challenges") val challenges: List<ChallengeHotness>
)

data class ChallengeHotness(
    @SerializedName("challenge") val challenge: Challenges, // 기본 챌린지 정보
    @SerializedName("challengeLikes") val challengeLikes: List<ChallengeLike>?, // 좋아요한 유저 정보 리스트
    @SerializedName("likes_yesterday") val likesYesterday: Int?, // 어제 받은 좋아요 수
)

data class ChallengeLike(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("challenge_id") val challengeId: Int,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?
)