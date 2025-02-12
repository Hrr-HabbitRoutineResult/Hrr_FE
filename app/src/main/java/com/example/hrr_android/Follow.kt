package com.example.hrr_android

data class Follow(
    val name: String,
    val level: String,
    val img: Int,               //대표 이미지
    var isFollower: Boolean = true,    //사용자를 팔로우 하는 사람인지
    var isFollowing: Boolean    //사용자가 팔로우 하는 사람인지
)
