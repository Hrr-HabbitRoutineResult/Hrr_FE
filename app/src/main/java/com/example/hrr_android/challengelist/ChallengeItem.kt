package com.example.hrr_android.challengelist

data class ChallengeItem(
    val title: String,               // 챌린지 제목
    val type: Boolean,       // 챌린지 유형 (true: 베이직, false: 스터디)
    val description: String,         // 챌린지 설명
    val frequency: String,           // 인증 빈도 (매일, 주 3회 등)
    val duration: String,            // 챌린지 기간 (6개월, 1년 등)
    val currentPeople: Int,          // 현재 참여 중인 인원
    val maxPeople: Int,              // 최대 참여 가능 인원
    val authType: Boolean = false    // 인증 유형 (true: 글, false: 사진)
)
