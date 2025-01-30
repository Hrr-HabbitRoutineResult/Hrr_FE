package com.example.hrr_android

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hrr_android.access.AuthViewModel
import com.example.hrr_android.access.ui.LoginActivity
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels() // ViewModel 사용

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)  // 스플래시 레이아웃 연결

        // 2초 대기 후 자동 로그인 여부 체크
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000) // 2초 대기

            if (authViewModel.hasSavedToken()) {
                moveToMainActivity() // 자동 로그인 → 메인 화면으로 이동
            } else {
                moveToLoginActivity() // 로그인 필요 → 로그인 화면으로 이동
            }
        }
    }

    private fun moveToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun moveToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
