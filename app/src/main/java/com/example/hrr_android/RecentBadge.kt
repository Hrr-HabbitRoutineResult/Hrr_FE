package com.example.hrr_android

import com.google.gson.annotations.SerializedName

data class RecentBadge(
    @SerializedName("badge_id") val badgeId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("icon") val icon: String,
    @SerializedName("type") val type: String
)
