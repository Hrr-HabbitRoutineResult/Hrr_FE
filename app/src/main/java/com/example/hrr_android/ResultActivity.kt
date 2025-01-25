package com.example.hrr_android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hrr_android.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰 바인딩 초기화
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewPager2 설정
        val challengeList = listOf(
            Challenge("달리기 하실 분", R.drawable.img_running, "가볍게 하루에 십 분을 목표로 하고 함께 달려보아요", true),
            Challenge("워드마스터 고등 베이직", R.drawable.img_english_book, "하루에 이틀 치 외워서 방학동안 워드마스터 끝내고 싶습니다!", true),
            Challenge("집밥", R.drawable.img_cook, "배달 그만 시키고 집밥 해먹어요 ㅎㅎ", true)
        )
        val adapter = OnboardingVPAdapter(challengeList)
        binding.vpOnboardingChallenge.adapter = adapter

        setupViewPagerPreview()

        binding.btnResultToHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupViewPagerPreview() {
        val pageMarginPx = 5
        val pagerWidth = 725
        val screenWidth = resources.displayMetrics.widthPixels // 스마트폰 너비 길이
        val offsetPx = screenWidth - pageMarginPx - pagerWidth

        binding.vpOnboardingChallenge.setPageTransformer { page, position ->
            // 중앙 정렬을 유지하면서 양옆 페이지도 동일한 크기로 유지
            page.translationX = position * -offsetPx
        }

        binding.vpOnboardingChallenge.offscreenPageLimit = 1
    }

}