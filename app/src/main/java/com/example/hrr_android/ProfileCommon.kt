package com.example.hrr_android

import androidx.fragment.app.FragmentActivity
import com.example.hrr_android.databinding.FragmentProfileBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class ProfileCommon {

    //ViewPager2 연결
    fun setupViewPager(binding: FragmentProfileBinding, activity: FragmentActivity, isMyProfile: Boolean) {
        val tabs = if (isMyProfile) arrayListOf("챌린지", "인증기록", "뱃지") else arrayListOf("챌린지", "인증기록")

        val profileVPAdapter = ProfileVPAdapter(activity, isMyProfile)
        binding.vpProfile.adapter = profileVPAdapter

        TabLayoutMediator(binding.tlProfile, binding.vpProfile) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }

    fun setupCircularProgressBar(binding: FragmentProfileBinding, earnedPoint: Int, requiredPoint: Int) {
        // 레벨 달성률 반원 게이지 바
        val circularProgressBar = binding.cpbProfileLevelGauge

        // 퍼센트 계산
        val progressPercentage = (earnedPoint / requiredPoint.toFloat()) * 50  // 반원이라 백분율 절반만 사용

        // CircularProgressBar 설정 ; 그라데이션, 시간
        circularProgressBar.apply {
            progressBarColorDirection = CircularProgressBar.GradientDirection.RIGHT_TO_LEFT
            setProgressWithAnimation(progressPercentage, 1000) // 1초 동안 애니메이션 적용
        }

    }
}