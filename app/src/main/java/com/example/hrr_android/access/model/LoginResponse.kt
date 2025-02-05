package com.example.hrr_android.access.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("resultType") val resultType: String,
    @SerializedName("error") val error: String?,
    @SerializedName("success") val success: SuccessData?
)

data class SuccessData(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String
)
