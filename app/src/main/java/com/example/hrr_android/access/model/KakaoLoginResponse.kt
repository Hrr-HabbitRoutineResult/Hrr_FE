package com.example.hrr_android.access.model

import com.google.gson.annotations.SerializedName

data class KakaoLoginResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String
)