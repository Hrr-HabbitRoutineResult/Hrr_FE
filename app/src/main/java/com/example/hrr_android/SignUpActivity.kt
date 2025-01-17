package com.example.hrr_android

import android.os.Bundle
import android.text.TextUtils.replace
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.hrr_android.databinding.ActivitySignUpBinding


class SignUpActivity : AppCompatActivity() {

    // 뷰 바인딩 선언
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰 바인딩 초기화
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 약관동의 프래그먼트로 시작
        if (savedInstanceState == null) {
            changeFragment(TermsFragment())
            updateProgress(25, "약관동의")
        }
    }

    // 프래그먼트 전환 함수
    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.layoutSignupFragment.id, fragment)
            .addToBackStack(null)
            .commit()
    }

    // 진행도 및 단계 업데이트 함수
    fun updateProgress(progress: Int, title: String) {
        binding.pbSignupStep.progress = progress   // ProgressBar 업데이트
        binding.tvSignupStepTitle.text = title   // 단계 업데이트
    }
}