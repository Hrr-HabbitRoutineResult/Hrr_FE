package com.example.hrr_android

import com.google.gson.annotations.SerializedName

data class HistoryResponse(
    @SerializedName("challengeId") val challengeId: Int = 0,    // 챌린지 id
    @SerializedName("name") val name: String = "",       // 챌린지 이름
    @SerializedName("verificationId") val verificationId: Int = 0,  // 인증 id
    @SerializedName("create_at") val createTime: String = "2025-02-09T22:27:02.000Z",     // 인증 시점
    @SerializedName("title") val title: String = "",  // 인증 게시글 제목
    @SerializedName("photoUrl") val photoUrl: String? = "",      // 인증 사진 uri
    @SerializedName("textUrl") val textUrl: String? = ""        // 인증 게시글에 포함된 링크
    // Todo: 바인딩에 사용하지 않는 요소들은
)
