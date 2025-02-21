package com.example.hrr_android

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id") val id: Int? = null,      // id는 기본값 설정하지 않음. 사용 시 처리 필요
    @SerializedName("nickname") val nickname: String = "User",
    @SerializedName("gender") val gender: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("level") val level: String? = "general",
    @SerializedName("points") val points: Int = 0,
    @SerializedName("followerCount") val followerCount: Int = 0,
    @SerializedName("followingCount") val followingCount: Int = 0,
    @SerializedName("user_badge_1") val userBadge1: BestBadge? = null,     // 뱃지도 사용 시 처리 필요
    @SerializedName("user_badge_2") val userBadge2: BestBadge? = null,
    @SerializedName("user_badge_3") val userBadge3: BestBadge? = null
)


data class BestBadge(
    val id: Int,
    val name: String,
    val icon: String,
    val type: String,
    val obtainedCount: Int
)