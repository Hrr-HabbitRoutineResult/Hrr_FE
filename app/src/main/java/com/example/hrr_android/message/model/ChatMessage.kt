package com.example.hrr_android.message.model

data class ChatMessage(
    val senderName: String, // 보낸 사람 이름
    val message: String,    // 메시지 내용
    val time: String,       // 보낸 시간
    val isMine: Boolean,    // 내가 보낸 메시지인지 여부
    val date: String        // 날짜
)