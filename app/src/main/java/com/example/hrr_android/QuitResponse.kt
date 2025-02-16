package com.example.hrr_android

import com.google.gson.annotations.SerializedName

data class QuitResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("isDeleted") val isDeleted: Boolean,
    @SerializedName("deletedAt") val deletedAt: String
)
