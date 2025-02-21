package com.example.hrr_android.challenge.model

data class WeeklyVerificationResponse(
    val challenge_id: String,
    val user_id: Int,
    val need_certified: List<String>,
    val checked_days: List<String>
)