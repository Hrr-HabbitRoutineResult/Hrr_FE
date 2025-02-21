package com.example.hrr_android.challenge.model

data class VerificationUploadResponse(
    val message: String,
    val verification: VerificationDetail
)

data class VerificationDetail(
    val verificationId: Int,
    val verificationType: String,
    val title: String,
    val photoUrl: String
)