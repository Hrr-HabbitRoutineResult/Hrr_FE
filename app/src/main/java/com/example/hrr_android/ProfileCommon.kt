package com.example.hrr_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
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

    //레벨 달성률 바인딩
    val levels: List<Level>
        = listOf(Level("general", 0, 30),
                 Level("bronze", 30, 80),
                 Level("silver", 80, 150),
                 Level("gold", 150, 250),
                 Level("master", 250, 400),
                 Level("challenger", 400, -1))   // 챌린저 달성 시 별도 처리 위해 -1로 설정
    fun setupCircularProgressBar(binding: FragmentProfileBinding, level:String, myPoint: Int) {
        // 레벨 달성률 반원 게이지 바
        val circularProgressBar = binding.cpbProfileLevelGauge

        // 퍼센트 계산
        val thisLevel = levels.find { it.grade == level }
        val earnedPoint = myPoint - (thisLevel?.gotPoint ?: 0) + 0.1    // ProgressBar에 미세하게 표시하기 위한 보정치 추가
        val requiredPoint = (thisLevel?.needPoint?: 1) - (thisLevel?.gotPoint ?: 0)
        val progressPercentage =
            if (level == "challenger") 50f // 챌린저 달성 시 full
            else (earnedPoint / requiredPoint).toFloat() * 50  // 반원이라 백분율 절반만 사용

        // CircularProgressBar 설정 ; 그라데이션, 시간
        circularProgressBar.apply {
            progressBarColorDirection = CircularProgressBar.GradientDirection.RIGHT_TO_LEFT
            setProgressWithAnimation(progressPercentage, 1000) // 1초 동안 애니메이션 적용
        }

    }

    //대표 뱃지 바인딩
    fun setupBadges(binding: FragmentProfileBinding, selectedBadges: List<Badge>) {
        when (selectedBadges.size) {
            0 -> {  }

            1 -> {
                binding.ivProfileBadge01.setImageResource(selectedBadges[0].icon)
                binding.tvProfileBadge01.text = selectedBadges[0].name
            }

            2 -> {
                binding.llProfileBadge02.visibility = View.VISIBLE

                binding.ivProfileBadge01.setImageResource(selectedBadges[0].icon)
                binding.tvProfileBadge01.text = selectedBadges[0].name

                binding.ivProfileBadge02.setImageResource(selectedBadges[1].icon)
                binding.tvProfileBadge02.text = selectedBadges[1].name
            }

            3 -> {
                binding.llProfileBadge02.visibility = View.VISIBLE
                binding.llProfileBadge03.visibility = View.VISIBLE

                binding.ivProfileBadge01.setImageResource(selectedBadges[0].icon)
                binding.tvProfileBadge01.text = selectedBadges[0].name

                binding.ivProfileBadge02.setImageResource(selectedBadges[1].icon)
                binding.tvProfileBadge02.text = selectedBadges[1].name

                binding.ivProfileBadge03.setImageResource(selectedBadges[2].icon)
                binding.tvProfileBadge03.text = selectedBadges[2].name
            }
        }
    }

    //팔로우 클릭 처리
    fun onFollowClicked(activity: FragmentActivity, view: View, type: String, isMyProfile: Boolean=true, ownerId: Int=0){
        view.setOnClickListener {
            val intent = Intent(activity, ProfileMoreActivity::class.java)
            intent.putExtra("type", type)
            intent.putExtra("isMyProfile", isMyProfile)
            intent.putExtra("id", ownerId)
            Log.d("otherDebug", "ProfileCommon - $ownerId")
            activity.startActivity(intent)
        }
    }
}