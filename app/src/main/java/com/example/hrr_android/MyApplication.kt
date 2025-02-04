package com.example.hrr_android

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class MyApplication : Application() {
    private val kakaoAppKey = BuildConfig.KAKAO_APP_KEY
    override fun onCreate() {
        super.onCreate()
        // Kakao SDK 초기화
        KakaoSdk.init(this, kakaoAppKey) // Kakao Developers에서 발급받은 앱 키로 변경
    }
}