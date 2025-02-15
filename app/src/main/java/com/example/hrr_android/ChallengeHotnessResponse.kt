package com.example.hrr_android

import com.google.gson.annotations.SerializedName

// API 응답 데이터 모델
data class ChallengeHotnessResponse(
    @SerializedName("challenges") val challenges: List<ChallengeHotness>
)

data class ChallengeHotness(
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
    @SerializedName("likesCount") val likesCount: Int,
    @SerializedName("challengeLikes") val challengeLikes: List<ChallengeLike>?,
    @SerializedName("frequencies") val frequencies: List<Frequency>,
    @SerializedName("likes_yesterday") val likesYesterday: Int?,
    @SerializedName("certification_frequency") val certificationFrequency: Any?,
    @SerializedName("challenge_duration") val challengeDuration: String?
)

data class ChallengeLike(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("challenge_id") val challengeId: Int,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?
)

data class Frequency(
    @SerializedName("id") val id: Int,
    @SerializedName("challenge_id") val challengeId: Int,
    @SerializedName("frequencyType") val frequencyType: String,
    @SerializedName("frequencyValue") val frequencyValue: String?,
    @SerializedName("monday") val monday: Boolean?,
    @SerializedName("tuesday") val tuesday: Boolean?,
    @SerializedName("wednesday") val wednesday: Boolean?,
    @SerializedName("thursday") val thursday: Boolean?,
    @SerializedName("friday") val friday: Boolean?,
    @SerializedName("saturday") val saturday: Boolean?,
    @SerializedName("sunday") val sunday: Boolean?
)