package com.example.hrr_android

data class UserResponse(
    val id: Int,
    val name: String,
    val gender: String,
    val email: String,
    val level: String,
    val points: Int,
    val followerCount: Int,
    val followingCount: Int,
    val badges: List<BadgeResponse>
)

data class BadgeResponse(
    val id: Int,
    val name: String,
    val icon: String,
    val type: String,
    val obtainedCount: Int
)