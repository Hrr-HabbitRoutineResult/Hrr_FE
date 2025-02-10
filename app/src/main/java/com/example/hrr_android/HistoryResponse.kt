package com.example.hrr_android

import com.google.gson.annotations.SerializedName

data class HistoryResponse(
    @SerializedName("challengeId") val challengeId: Int,    // 챌린지 id
    @SerializedName("name") val name: String,       // 챌린지 이름
    @SerializedName("verificationId") val verificationId: Int,  // 인증 id
    @SerializedName("create_at") val createTime: String,     // 인증 시점
    @SerializedName("title") val title: String,  // 인증 게시글 제목
    @SerializedName("photoUrl") val photoUrl: String?,      // 인증 사진 uri
    @SerializedName("textUrl") val textUrl: String?        // 인증 게시글에 포함된 링크
)
