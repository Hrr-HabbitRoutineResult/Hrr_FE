package com.example.hrr_android

data class Post(
    var challengeName: String,
    var postTitle: String = "",         //게시글 제목
    var date: String,
    var coverimg: Int? = null,
    var hasLink: Boolean = false,    //링크 포함 여부
    var comment: String = ""
)
