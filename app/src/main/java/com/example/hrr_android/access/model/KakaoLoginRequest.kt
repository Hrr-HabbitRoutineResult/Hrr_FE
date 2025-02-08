package com.example.hrr_android.access.model

import com.google.gson.annotations.SerializedName

data class KakaoLoginRequest(
    @SerializedName("kakao_token") val kakaoToken: String
)