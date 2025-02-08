package com.example.hrr_android.access.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hrr_android.access.ui.fragment.CompleteFragment
import com.example.hrr_android.access.ui.fragment.InfoInputFragment
import com.example.hrr_android.access.ui.fragment.NicknameFragment
import com.example.hrr_android.access.ui.fragment.TermsFragment
import com.example.hrr_android.databinding.ActivitySignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

        // 앱의 뒤로 가기 버튼 동작
        binding.btnSignupBack.setOnClickListener {
            handleBackPressed()
        }

        // 휴대폰 자체의 뒤로 가기 동작
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPressed()
            }
        })

        // BackStack 변경 감지 리스너 추가
        supportFragmentManager.addOnBackStackChangedListener {
            updateSignUpFragment()
        }
    }

    private fun handleBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(binding.layoutSignupFragment.id) ?: return
        if (currentFragment is TermsFragment) {  // 약관동의 화면에서는 로그인 화면으로 이동
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
            finish()
        } else {  // 그 외의 경우에는 프래그먼트로 뒤로가기
            supportFragmentManager.popBackStack()
            updateSignUpFragment()
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

    // 현재 프래그먼트에 맞게 UI 업데이트
    private fun updateSignUpFragment() {
        val currentFragment = supportFragmentManager.findFragmentById(binding.layoutSignupFragment.id)

        when (currentFragment) {
            is TermsFragment -> updateProgress(33, "약관동의")
            is InfoInputFragment, is NicknameFragment -> updateProgress(66, "가입정보 입력")
            is CompleteFragment -> updateProgress(100, "가입완료")
        }
    }
}