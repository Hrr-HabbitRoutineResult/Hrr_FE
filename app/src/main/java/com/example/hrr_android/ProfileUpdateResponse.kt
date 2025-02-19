package com.example.hrr_android

import com.google.gson.annotations.SerializedName

data class ProfileUpdateResponse(
    @SerializedName("name") val name: String,
    @SerializedName("profilePhoto") val profilePhoto: String,
    @SerializedName("badges") val badges: List<Int?> // `null` 허용
)
