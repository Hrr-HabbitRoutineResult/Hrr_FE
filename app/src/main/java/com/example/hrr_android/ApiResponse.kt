package com.example.hrr_android

data class ApiResponse<T>(
    val resultType: String,
    val error: String?,  // null이면 성공
    val success: Success<T>? // 성공 응답 내용
)

data class Success<T>(
    val data: T? // 실제 데이터
)