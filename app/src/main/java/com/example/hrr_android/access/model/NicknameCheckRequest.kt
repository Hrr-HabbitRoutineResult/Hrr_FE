package com.example.hrr_android.access.model

import com.google.gson.annotations.SerializedName

data class NicknameCheckRequest(
    @SerializedName("nickname") val nickname: String
)
