package com.example.hrr_android

data class TempUser(
    val name: String,
    val level: String,
    val img: Int,               //대표 이미지
    var isBlocked: Boolean = true
)
