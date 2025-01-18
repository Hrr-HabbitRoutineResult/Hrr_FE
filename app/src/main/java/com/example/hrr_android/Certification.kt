package com.example.hrr_android

data class Certification(
    var challengeName: String,
    var title: String,
    var date: String,
    var coverimg: Int? = null,
    var hasLink: Boolean
)
