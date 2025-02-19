package com.example.hrr_android.access.model

import com.google.gson.annotations.SerializedName

data class PasswordCheckRequest(
    @SerializedName("password") val password: String
)
