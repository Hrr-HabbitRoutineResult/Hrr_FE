package com.example.hrr_android.makechallenge

import com.google.gson.annotations.SerializedName

data class MakeChallengeRequest(
    @SerializedName("name") val name: String,
    @SerializedName("owner_id") val ownerId: Int,
    @SerializedName("type") val type: String,
    @SerializedName("description") val description: String,
    @SerializedName("challengeImage") val challengeImage: String,
    @SerializedName("category") val category: String,
    @SerializedName("challengeStatus") val challengeStatus: String,
    @SerializedName("maxParticipants") val maxParticipants: Int?,
    @SerializedName("verificationType") val verificationType: String,
    @SerializedName("rule") val rule: String,

    // `type`이 "basic"이면 joinDate & endDate는 null, duration & frequencyValue는 존재
    @SerializedName("joinDate") val joinDate: String?,
    @SerializedName("endDate") val endDate: String?,
    @SerializedName("duration") val duration: String?,
    @SerializedName("frequencyType") val frequencyType: String,
    @SerializedName("frequencyValue") val frequencyValue: String?,

    // `type`이 "study"일 때 특정 요일 선택 가능
    @SerializedName("days") val days: List<String>?,

    @SerializedName("keywords") val keywords: List<String>
)