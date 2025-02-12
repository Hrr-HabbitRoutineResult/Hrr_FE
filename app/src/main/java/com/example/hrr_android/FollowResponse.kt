package com.example.hrr_android

import com.google.gson.annotations.SerializedName


data class FollowResponse(
    @SerializedName("followers") val followers: List<FollowInfo>? = null,
    @SerializedName("followings") val followings: List<FollowInfo>? = null
)

data class FollowInfo(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("nickname") val nickname: String = "",
    @SerializedName("profilePhoto") val profilePhoto: String = ""
)