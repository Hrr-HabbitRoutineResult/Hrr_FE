package com.example.hrr_android.community.model

data class Post(
    var title: String,
    var content: String,
    val author: String,
    var like: Int,
    var comment: Int,
    val createdAt: String,
    val community: String
)
