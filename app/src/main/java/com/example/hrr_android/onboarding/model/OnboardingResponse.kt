package com.example.hrr_android.onboarding.model

import com.google.gson.annotations.SerializedName

data class OnboardingResponse(
    @SerializedName("challenge_id") val id: Int,
    @SerializedName("name")val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("challengeImage") val challengeImage: String,
    @SerializedName("type") val type: String
)
