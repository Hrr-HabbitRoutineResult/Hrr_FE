package com.example.hrr_android

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.hrr_android.databinding.FragmentProfileBinding
import com.google.android.material.tabs.TabLayoutMediator

class ProfileCommon {

    fun setupViewPager(binding: FragmentProfileBinding, activity: FragmentActivity, isMyProfile: Boolean) {
        val tabs = if (isMyProfile) arrayListOf("챌린지", "인증기록", "뱃지") else arrayListOf("챌린지", "인증기록")

        val profileVPAdapter = ProfileVPAdapter(activity, isMyProfile)
        binding.vpProfile.adapter = profileVPAdapter

        TabLayoutMediator(binding.tlProfile, binding.vpProfile) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }
}