package com.example.hrr_android.access.model

import com.google.gson.annotations.SerializedName

data class KakaoLoginResponse(
    @SerializedName("userId") val userId: Int,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String
)