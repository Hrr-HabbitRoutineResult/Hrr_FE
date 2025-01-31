package com.example.hrr_android.access.model

data class EmailConfirmResponse(
    val id: Int,
    val email: String,
    val verified: Boolean
)
