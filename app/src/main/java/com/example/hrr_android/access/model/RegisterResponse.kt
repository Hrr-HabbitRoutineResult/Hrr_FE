package com.example.hrr_android.access.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String
)