package com.example.hrr_android

data class Challenge(
    var title: String = "",
    var coverimg: Int,
    var description: String= "",        //챌린지 한 줄 소개
    var isCertified: Boolean = false,    //인증 완료 여부
    val isTypeBasic: Boolean = false    // 챌린지 타입 (true: 베이직, false: 스터디)
)
