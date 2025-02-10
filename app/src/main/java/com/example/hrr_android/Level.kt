package com.example.hrr_android

data class Level(
    val grade: String = "general",
    val gotPoint: Int = 0,     // 지금 레벨 달성 포인트
    val needPoint: Int = 0     // 다음 레벨 달성 포인트
)
