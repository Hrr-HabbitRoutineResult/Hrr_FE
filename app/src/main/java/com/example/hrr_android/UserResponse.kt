package com.example.hrr_android

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("email") val email: String,
    @SerializedName("level") val level: String?,
    @SerializedName("points") val points: Int,
    @SerializedName("followerCount") val followerCount: Int,
    @SerializedName("followingCount") val followingCount: Int,
    @SerializedName("user_badge_1") val userBadge1: BadgeResponse?,
    @SerializedName("user_badge_2") val userBadge2: BadgeResponse?,
    @SerializedName("user_badge_3") val userBadge3: BadgeResponse?
)


data class BadgeResponse(
    val id: Int,
    val name: String,
    val icon: String,
    val type: String,
    val obtainedCount: Int
)