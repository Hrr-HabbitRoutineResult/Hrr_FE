package com.example.hrr_android

data class Community(
    val isPinned: Boolean, // 핀 여부
    val title: String,     // 게시판 제목
    val description: String, // 게시판 설명
    val parentCategory: BaseCommunity? = null  // 소속 기본 게시판
)