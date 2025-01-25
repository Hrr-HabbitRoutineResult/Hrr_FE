package com.example.hrr_android

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hrr_android.databinding.ActivityOnboardingBinding
import com.google.android.material.snackbar.Snackbar

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰 바인딩 초기화
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기 프래그먼트 설정
        if (savedInstanceState == null) {
            changeFragment(InfoSelectFragment())
        }

        // Back 버튼 클릭 리스너
        binding.btnOnboardingBack.setOnClickListener {
            handleBackPressed()
        }

        // Next 버튼 클릭 리스너
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
    }

    private fun navigateToNextFragment() {
        val currentFragment = supportFragmentManager.findFragmentById(binding.layoutOnboardingFragmentContainer.id)

        // 현재 프래그먼트가 특정 타입인지 확인
        when (currentFragment) {
            is InfoSelectFragment -> {
                if (currentFragment.isValidSelection()) {
                    changeFragment(CategoryFragment())
                } else {
                    showSnackbar("정보를 선택해주세요.")
                }
            }
            is CategoryFragment -> {
                if (currentFragment.isValidSelection()) {
                    changeFragment(GoalFragment())
                } else {
                    showSnackbar("카테고리를 선택해주세요.")
                }
            }
            is GoalFragment -> {
                if (currentFragment.isValidSelection()) {
                    // ResultActivity로 이동 (LoadingActivity를 통해)
                    val intent = Intent(this, LoadingActivity::class.java)
                    intent.putExtra("fromOnboarding", true) // 온보딩에서 왔음을 전달
                    startActivity(intent)
                    finish() // OnboardingActivity 종료
                } else {
                    showSnackbar("목표를 선택해주세요.")
                }
            }

        }
    }


    // 진행도 및 단계 업데이트
    private fun updateProgress(progress: Int, title: String, describe: String) {
        binding.pbOnboardingStep.progress = progress // ProgressBar 업데이트
        binding.tvOnboardingStepTitle.text = title   // 단계 텍스트 업데이트
        binding.tvOnboardingStepDescribe.text = describe  // 설명 텍스트 업데이트
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

    private fun showSnackbar(message: String) {
        Snackbar.make(
            binding.root, // 루트 레이아웃
            message, // 메시지
            Snackbar.LENGTH_SHORT // 지속 시간
        ).setAnchorView(binding.btnOnboardingNext) // Next 버튼을 기준으로 표시
            .show()
    }

}
