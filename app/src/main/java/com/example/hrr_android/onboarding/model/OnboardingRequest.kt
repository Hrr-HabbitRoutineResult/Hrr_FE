package com.example.hrr_android.onboarding.model

import com.google.gson.annotations.SerializedName

data class OnboardingRequest(
    @SerializedName("type") val type: String
)
