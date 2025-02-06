package com.example.hrr_android

import android.app.Application

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AuthRepositoryProvider.initialize(this) // 앱 시작 시 Repository 초기화
    }
}