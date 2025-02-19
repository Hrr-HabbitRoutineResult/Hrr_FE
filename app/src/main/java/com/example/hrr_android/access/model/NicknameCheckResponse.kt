package com.example.hrr_android.access.model

import com.google.gson.annotations.SerializedName

data class NicknameCheckResponse(
    @SerializedName("resultType") val resultType: String,
    @SerializedName("error") val error: NicknameError?,
    @SerializedName("success") val success: NicknameCheckSuccess?
)

data class NicknameError(
    @SerializedName("errorCode") val errorCode: String,
    @SerializedName("reason") val reason: String
)

data class NicknameCheckSuccess(
    @SerializedName("check") val check: Boolean
)