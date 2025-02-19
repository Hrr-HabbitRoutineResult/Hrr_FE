package com.example.hrr_android.onboarding.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.hrr_android.MainActivity
import com.example.hrr_android.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    private var backPressedOnce = false     // 뒤로가기 버튼 상태 저장 변수
    private val handler = Handler(Looper.getMainLooper())   // 시간 초과 처리

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIntroToOnboarding.setOnClickListener {
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnIntroToHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 시스템 뒤로가기 버튼을 감지해서 두 번 눌렀을 때 종료 실행
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedOnce) {
                    finish() // 앱 종료
                } else {
                    backPressedOnce = true
                    Toast.makeText(this@IntroActivity, "\"뒤로\" 버튼 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()

                    // 2초 후 다시 false로 변경하여 재입력 요구
                    handler.postDelayed({ backPressedOnce = false }, 2000)
                }
            }
        })
    }
}