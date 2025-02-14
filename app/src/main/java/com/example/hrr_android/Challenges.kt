package com.example.hrr_android

import com.google.gson.annotations.SerializedName

data class Challenges(
    @SerializedName("id") val id: Int,
    @SerializedName("owner_id") val ownerId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("description") val description: String,
    @SerializedName("challengeImage") val challengeImage: String,
    @SerializedName("challengeStatus") val challengeStatus: String,
    @SerializedName("currentParticipants") val currentParticipants: Int?,
    @SerializedName("maxParticipants") val maxParticipants: Int?,
    @SerializedName("verificationType") val verificationType: String,
    @SerializedName("rule") val rule: String?,
    @SerializedName("joinDate") val joinDate: String?,
    @SerializedName("endDate") val endDate: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("duration") val duration: String?,
    @SerializedName("category") val category: String?,
    @SerializedName("likesCount") val likesCount: Int
)
