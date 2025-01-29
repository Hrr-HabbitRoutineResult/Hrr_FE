package com.example.hrr_android.onboarding.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hrr_android.LoadingActivity
import com.example.hrr_android.access.ValidUtils
import com.example.hrr_android.databinding.ActivityOnboardingBinding
import com.example.hrr_android.onboarding.ui.fragment.CategoryFragment
import com.example.hrr_android.onboarding.ui.fragment.GoalFragment
import com.example.hrr_android.onboarding.ui.fragment.InfoSelectFragment

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰 바인딩 초기화
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 기본적으로 버튼을 비활성화
        updateNextButtonState(null)

        // 초기 프래그먼트 설정
        if (savedInstanceState == null) {
            changeFragment(InfoSelectFragment())
        }

        // "뒤로 가기" 버튼 클릭 리스너
        binding.btnOnboardingBack.setOnClickListener {
            handleBackPressed()
        }

        // "다음" 버튼 클릭 리스너
        binding.btnOnboardingNext.setOnClickListener {
            navigateToNextFragment()
        }

        // 디바이스 뒤로가기 버튼 동작
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPressed()
            }
        })

        // BackStack 변경 감지 리스너 추가
        supportFragmentManager.addOnBackStackChangedListener {
            updateCurrentFragment()
        }
    }

    // 뒤로가기 동작 처리
    private fun handleBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(binding.layoutOnboardingFragmentContainer.id) ?: return
        if (currentFragment is InfoSelectFragment) { // 처음 단계에서 뒤로가기 시 종료
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
            finish()
        } else { // 이전 단계로 이동
            supportFragmentManager.popBackStack()
            updateCurrentFragment()
        }
    }

    // 프래그먼트 전환 함수
    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.layoutOnboardingFragmentContainer.id, fragment)
            .addToBackStack(null)
            .commit()

        updateNextButtonState(null)
    }

    // 현재 프래그먼트의 상태에 따라 다음 화면으로 이동
    private fun navigateToNextFragment() {
        val currentFragment = supportFragmentManager.findFragmentById(binding.layoutOnboardingFragmentContainer.id)

        when (currentFragment) {
            is InfoSelectFragment -> if (currentFragment.isValidSelection()) changeFragment(CategoryFragment())
            is CategoryFragment -> if (currentFragment.isValidSelection()) changeFragment(GoalFragment())
            is GoalFragment -> if (currentFragment.isValidSelection()) {
                val intent = Intent(this, LoadingActivity::class.java)
                intent.putExtra("fromOnboarding", true)
                startActivity(intent)
                finish()
            }
        }
    }

    // 현재 프래그먼트의 상태에 따라 버튼 활성화/비활성화 업데이트
    fun updateNextButtonState(currentFragment: Fragment?) {
        val isEnabled = when (currentFragment) {
            is InfoSelectFragment -> currentFragment.isValidSelection()
            is CategoryFragment -> currentFragment.isValidSelection()
            is GoalFragment -> currentFragment.isValidSelection()
            else -> false
        }

        ValidUtils.updateButtonState(
            binding.btnOnboardingNext,
            binding.tvOnboardingNext,
            binding.ivOnboardingNext,
            isEnabled
        )
    }

    // 진행도 및 단계 업데이트
    private fun updateProgress(progress: Int, title: String, describe: String) {
        binding.pbOnboardingStep.progress = progress
        binding.tvOnboardingStepTitle.text = title
        binding.tvOnboardingStepDescribe.text = describe
    }

    // 현재 프래그먼트에 맞게 UI 업데이트
    private fun updateCurrentFragment() {
        val currentFragment = supportFragmentManager.findFragmentById(binding.layoutOnboardingFragmentContainer.id)
        when (currentFragment) {
            is InfoSelectFragment -> updateProgress(33, "안녕하세요!", "아래에서 본인의 특성을 선택해주세요.")
            is CategoryFragment -> updateProgress(66, "관심 있는 분야가 무엇인가요?", "아래의 카테고리에서 가장 관심있는 분야를 선택해주세요.")
            is GoalFragment -> updateProgress(100, "나의 목표는...", "아래에서 챌린지를 통해 이루고 싶은 목표를 선택해주세요. (최대 3개)")
        }
    }
}
