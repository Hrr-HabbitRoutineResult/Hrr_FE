package com.example.hrr_android

import com.google.gson.annotations.SerializedName

data class ChallengeEndResponse(
    val completedChallenges: List<ChallengesEnd>
)

data class ChallengesEnd(
    @SerializedName("challenge_id") val id: Int,
    @SerializedName("name")val name: String,
    @SerializedName("challengeImage") val imageUrl: String,
    @SerializedName("description") val description: String
)
