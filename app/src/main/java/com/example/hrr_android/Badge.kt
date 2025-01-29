package com.example.hrr_android

data class Badge(
    var name: String,   //뱃지명
    var icon: Int,
    var isObtained: Boolean = false,     //획득 여부
    var type: String
)
