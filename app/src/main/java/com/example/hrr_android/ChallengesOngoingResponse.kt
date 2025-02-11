package com.example.hrr_android

import com.google.gson.annotations.SerializedName

data class ChallengesOngoingResponse(
    @SerializedName("ongoingChallenges") val ongoingChallenges: List<ChallengesOngoing>
)

data class ChallengesOngoing(
    @SerializedName("challenge_id") val challengeId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("challengeImage") val image: String,
    @SerializedName("type") val type: String,
    @SerializedName("verificatedToday") val verification: Boolean
)