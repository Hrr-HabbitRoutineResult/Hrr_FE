package com.example.hrr_android.access.model

import com.google.gson.annotations.SerializedName

data class PasswordResetRequest (
    @SerializedName("email") val email: String
)