package com.example.hrr_android.makechallenge

import com.google.gson.annotations.SerializedName

data class MakeChallengeResponse(
    @SerializedName("result") val result: MakeChallenge
)

data class MakeChallenge(
    @SerializedName("challenge") val challenge: Challenge,
    @SerializedName("keywords") val keywords: List<Keyword>,
    @SerializedName("frequency") val frequency: Frequency
)

data class Challenge(
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
    @SerializedName("duration") val duration: String?,
    @SerializedName("category") val category: String?,
    @SerializedName("likesCount") val likesCount: Int,
    @SerializedName("scrapsCount") val scrapsCount: Int,

    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?
)

data class Keyword(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

data class Frequency(
    @SerializedName("frequencyType") val frequencyType: String,
    @SerializedName("frequencyValue") val frequencyValue: String?,

    @SerializedName("monday") val monday: Boolean = false,
    @SerializedName("tuesday") val tuesday: Boolean = false,
    @SerializedName("wednesday") val wednesday: Boolean = false,
    @SerializedName("thursday") val thursday: Boolean = false,
    @SerializedName("friday") val friday: Boolean = false,
    @SerializedName("saturday") val saturday: Boolean = false,
    @SerializedName("sunday") val sunday: Boolean = false
)