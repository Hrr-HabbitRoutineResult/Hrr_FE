package com.example.hrr_android.access.model

import com.google.gson.annotations.SerializedName

data class PasswordCheckResponse(
    @SerializedName("message") val message: String
)
