package com.example.hrr_android

import com.google.gson.annotations.SerializedName

data class BadgeConditionResponse(
    @SerializedName("success") val success: List<BadgeCondition>
)

data class BadgeCondition(
    @SerializedName("badgeId") val badgeId: Int,
    @SerializedName("conditionId") val conditionId: Int,
    @SerializedName("description") val description: String,
    @SerializedName("isAchieved") val isAchieved: Boolean
)

