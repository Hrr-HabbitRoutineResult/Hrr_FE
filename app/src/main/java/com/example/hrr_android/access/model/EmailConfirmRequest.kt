package com.example.hrr_android.access.model

data class EmailConfirmRequest(
    val email: String,
    val verificationCode: String
)
