package com.example.hrr_android.challenge.model

data class PostResponse(
    val id: Int,
    val user_id: Int,
    val userChallenge_id: Int,
    val verificationType: String,
    val photoUrl: String,
    val title: String,
    val content: String?,
    val question: Boolean,
    val textUrl: String?,
    val created_at: String,
    val updated_at: String?,
    val adoptionComplete: Boolean,
    val verificationStatus: String,
    val likesCount: Int,
    val commentsCount: Int,
    val scrapsCount: Int,
    val comment: List<Comment>
)

data class Comment(
    val id: Int,
    val user_id: Int,
    val nickname: String,
    val content: String,
    val parent_id: Int?,
    val created_at: String,
    val updated_at: String?,
    val selected: Boolean,
    val anonymous: Boolean,
    val replies: List<Comment>
)