package com.example.hrr_android.onboarding.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class OnboardingResponse<T>(
    val resultType: String,
    val error: String?,  // null이면 성공
    val success: List<T>? // 성공 응답 내용
)

@Parcelize
data class OnboardingSuccess(
    @SerializedName("challenge_id") val challengeId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("challengeImage") val challengeImage: String,
    @SerializedName("type") val type: String
) : Parcelable
