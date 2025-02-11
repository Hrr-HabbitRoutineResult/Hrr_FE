package com.example.hrr_android.access.model

import com.google.gson.annotations.SerializedName

data class TokenRequest(
    @SerializedName("refreshToken") val refreshToken: String
)