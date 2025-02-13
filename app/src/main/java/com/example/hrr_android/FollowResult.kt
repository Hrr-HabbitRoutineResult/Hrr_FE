package com.example.hrr_android

import com.google.gson.annotations.SerializedName

data class FollowResult(
    @SerializedName("message") val message: String,
    @SerializedName("followedUserId") val followedUserId: Int? = null,
    @SerializedName("unfollowedUserId") val unfollowedUserId: Int? = null
)
