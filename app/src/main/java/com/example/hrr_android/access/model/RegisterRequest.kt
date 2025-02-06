package com.example.hrr_android.access.model

data class RegisterRequest(
    val email: String,
    val password: String,
    val nickname: String,
    val verificationId: Int
)
