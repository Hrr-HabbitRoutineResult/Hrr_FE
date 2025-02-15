package com.example.hrr_android.access.model

import com.google.gson.annotations.SerializedName

data class EmailVerificationResponse(
    @SerializedName("resultType") val resultType: String,
    @SerializedName("error") val error: String?,  // null이면 성공
    @SerializedName("success") val success: String? // success를 String으로 변경
)