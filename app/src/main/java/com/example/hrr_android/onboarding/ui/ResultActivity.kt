package com.example.hrr_android.onboarding.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.hrr_android.MainActivity
import com.example.hrr_android.OnChallengeClickListener
import com.example.hrr_android.onboarding.adapter.OnboardingVPAdapter
import com.example.hrr_android.databinding.ActivityResultBinding
import com.example.hrr_android.onboarding.model.OnboardingResponse
import com.example.hrr_android.onboarding.model.OnboardingSuccess

class ResultActivity : AppCompatActivity(), OnChallengeClickListener {

    private lateinit var binding: ActivityResultBinding
    private var allChallenges: ArrayList<OnboardingSuccess> = arrayListOf()
    private var displayedChallenges: ArrayList<OnboardingSuccess> = arrayListOf()

    private var backPressedOnce = false     // 뒤로가기 버튼 상태 저장 변수
    private val handler = Handler(Looper.getMainLooper())   // 시간 초과 처리

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // LoadingActivity에서 전달한 API 응답 데이터 받기
        val responseList: ArrayList<OnboardingSuccess>? = intent.getParcelableArrayListExtra("response_data")
        val errorMessage = intent.getStringExtra("error_message")

        if (!responseList.isNullOrEmpty()) {
            val adapter = OnboardingVPAdapter(responseList, this)
            binding.vpOnboardingChallenge.adapter = adapter
        } else {
            if (errorMessage != null) {
                Log.e("ResultActivity", errorMessage)
            }
        }

        setupViewPagerPreview()

        // LoadingActivity에서 전달된 더미 데이터를 받아옴
        allChallenges = intent.getParcelableArrayListExtra("response_data") ?: arrayListOf()


        binding.btnResultToHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 새로고침 버튼 클릭 시 새로운 데이터 표시
        binding.btnResultReset.setOnClickListener {
            updateDisplayedChallenges()
        }

        // 시스템 뒤로가기 버튼을 감지해서 두 번 눌렀을 때 종료 실행
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedOnce) {
                    finish() // 앱 종료
                } else {
                    backPressedOnce = true
                    Toast.makeText(this@ResultActivity, "\"뒤로\" 버튼 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()

                    // 2초 후 다시 false로 변경하여 재입력 요구
                    handler.postDelayed({ backPressedOnce = false }, 2000)
                }
            }
        })
    }

    // 새로운 3개의 챌린지를 랜덤으로 선택하여 ViewPager2에 적용
    private fun updateDisplayedChallenges() {
        if (allChallenges.isNotEmpty()) {
            displayedChallenges = ArrayList(allChallenges.shuffled().take(3)) // 랜덤 3개 선택
            val adapter = OnboardingVPAdapter(displayedChallenges, this)
            binding.vpOnboardingChallenge.adapter = adapter
        }
    }

    private fun setupViewPagerPreview() {
        val pageMarginPx = 5
        val pagerWidth = 725
        val screenWidth = resources.displayMetrics.widthPixels
        val offsetPx = screenWidth - pageMarginPx - pagerWidth

        binding.vpOnboardingChallenge.setPageTransformer { page, position ->
            page.translationX = position * -offsetPx
        }

        binding.vpOnboardingChallenge.offscreenPageLimit = 1
    }

    override fun onItemClick(challengeId: Int) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("navigate_to", "challengeFragment") // ChallengeFragment로 이동할 정보 추가
            putExtra("challenge_id", challengeId) // Challenge ID 전달
        }
        startActivity(intent)
        finish()
    }
}
