package com.example.hrr_android.challenge.model

data class VerificationRequest(
    val photoUrl: String,
    val title: String,
    val content: String,
    val textUrl: String?,
    val question: Int
)