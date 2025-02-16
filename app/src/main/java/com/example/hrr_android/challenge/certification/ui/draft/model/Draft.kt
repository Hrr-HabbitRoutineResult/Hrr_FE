package com.example.hrr_android.challenge.certification.ui.draft.model

data class Draft(
    val id: Long,
    val title: String,
    val content: String,
    val date: String,
    val type: String  // TEXT, PHOTO 등 인증 타입
)