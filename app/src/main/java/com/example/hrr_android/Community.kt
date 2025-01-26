package com.example.hrr_android

data class Community(
    val isPinned: Boolean, // 핀 여부
    val title: String,     // 게시판 제목
    val description: String // 게시판 설명
)