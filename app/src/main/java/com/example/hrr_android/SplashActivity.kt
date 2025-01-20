package com.example.hrr_android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)  // 스플래시 레이아웃 연결

        // 2초 후 MainActivity로 전환
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)  // 2초 대기
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()  // 스플래시 액티비티 종료
        }
    }
}