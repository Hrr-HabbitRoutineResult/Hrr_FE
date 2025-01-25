package com.example.hrr_android

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.hrr_android.databinding.ActivityLoadingBinding
import com.example.hrr_android.onboarding.ui.ResultActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰 바인딩 초기화
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 온보딩에서 왔는지 확인
        val fromOnboarding = intent.getBooleanExtra("fromOnboarding", false)

        if (fromOnboarding) {
            // 온보딩에서 왔다면 텍스트 변경 및 버튼 숨기기
            binding.tvLoadingTitle.text = "잠시만 기다려주세요!"
            binding.tvLoadingDescribe.text = "김흐르 님에게 알맞는 챌린지를 찾고 있어요.\n바로 찾아서 알려드릴게요."
            binding.btnOnboardingNext.visibility = View.GONE

            // 2초 후 ResultActivity로 전환
            CoroutineScope(Dispatchers.Main).launch {
                delay(2000)  // 2초 대기
                startActivity(Intent(this@LoadingActivity, ResultActivity::class.java))
                finish()  // 로딩 액티비티
            }
        }
    }
}
