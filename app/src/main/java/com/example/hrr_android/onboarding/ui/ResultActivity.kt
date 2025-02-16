package com.example.hrr_android.onboarding.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.hrr_android.MainActivity
import com.example.hrr_android.onboarding.adapter.OnboardingVPAdapter
import com.example.hrr_android.databinding.ActivityResultBinding
import com.example.hrr_android.onboarding.model.OnboardingResponse
import com.example.hrr_android.onboarding.model.OnboardingSuccess

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private var allChallenges: ArrayList<OnboardingSuccess> = arrayListOf()
    private var displayedChallenges: ArrayList<OnboardingSuccess> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // LoadingActivity에서 전달한 API 응답 데이터 받기
        val responseList: ArrayList<OnboardingSuccess>? = intent.getParcelableArrayListExtra("response_data")
        val errorMessage = intent.getStringExtra("error_message")

        if (!responseList.isNullOrEmpty()) {
            val adapter = OnboardingVPAdapter(responseList)
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
    }

    // 새로운 3개의 챌린지를 랜덤으로 선택하여 ViewPager2에 적용
    private fun updateDisplayedChallenges() {
        if (allChallenges.isNotEmpty()) {
            displayedChallenges = ArrayList(allChallenges.shuffled().take(3)) // 랜덤 3개 선택
            val adapter = OnboardingVPAdapter(displayedChallenges)
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
}
