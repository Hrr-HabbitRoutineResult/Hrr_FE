package com.example.hrr_android

import com.google.gson.annotations.SerializedName

data class BlockResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("blocker_id") val blockerId: Int,
    @SerializedName("blocked_id") val blockedId: Int,
    @SerializedName("created_at") val createdAt: String
)
