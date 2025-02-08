package com.example.hrr_android

data class Badge(
    var name: String,   //뱃지명
    var icon: Int,
    var isObtained: Boolean = false,     //획득 여부
    val type: String = "",    //종류 : 유형, 카테고리
    val obtainCondition: List<Condition> = emptyList(),  //획득 조건, 달성 여부
    var isSelected: Boolean = false
)

data class Condition(
    val description: String = "", // 조건 설명
    val isObtained: Boolean = false    // 달성 여부
)
