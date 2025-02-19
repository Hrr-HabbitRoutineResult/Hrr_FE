package com.example.hrr_android

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import java.security.NoSuchAlgorithmException
import android.util.Base64
import androidx.annotation.RequiresApi
import java.security.MessageDigest


@HiltAndroidApp
class MyApplication : Application() {
    private val kakaoAppKey = BuildConfig.KAKAO_APP_KEY
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate() {
        super.onCreate()
        // Kakao SDK 초기화
        KakaoSdk.init(this, kakaoAppKey) // Kakao Developers에서 발급받은 앱 키로 변경

        // 앱 실행 시 자동으로 해시키 출력
        getKeyHash()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun getKeyHash() {
        try {
            val info = packageManager.getPackageInfo(
                packageName, PackageManager.GET_SIGNING_CERTIFICATES
            )
            for (signature in info.signingInfo?.apkContentsSigners!!) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val keyHash = Base64.encodeToString(md.digest(), Base64.NO_WRAP)
                Log.d("KakaoLogin", "키 해시: $keyHash")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("KakaoLogin", "SHA 알고리즘을 찾을 수 없음", e)
        } catch (e: Exception) {
            Log.e("KakaoLogin", "키 해시 가져오기 실패", e)
        }
    }
}