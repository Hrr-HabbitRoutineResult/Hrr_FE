package com.example.hrr_android.access.model

import com.google.gson.annotations.SerializedName

data class TokenResponse (
    @SerializedName("access_token") val accessToken: String
)