package com.example.hrr_android

data class ChallengeToday(
    val title: String,
    val categoryIconRes: Int,
    val description: String,
    val participantsNow: Int,
    val participantsMax: Int,
    val cycle: String,
    val period: String
)
