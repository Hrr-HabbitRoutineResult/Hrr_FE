package com.example.hrr_android

import com.google.gson.annotations.SerializedName

data class BadgeResponse(
    @SerializedName("typeBadges") val typeBadges: List<BadgeInfo>,
    @SerializedName("categoryBadges") val categoryBadges: List<BadgeInfo>
)

data class BadgeInfo(
    @SerializedName("badgeId") val badgeId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("icon") val icon: String,
    @SerializedName("isObtained") val isObtained: Boolean
)
