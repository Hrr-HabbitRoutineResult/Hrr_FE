package com.example.hrr_android.onboarding.model

import com.google.gson.annotations.SerializedName

data class OnboardingRequest(
    @SerializedName("category") val category: String
)
