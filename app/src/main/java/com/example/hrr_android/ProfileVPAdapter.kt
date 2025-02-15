package com.example.hrr_android

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProfileVPAdapter(activity: FragmentActivity, private val isMyProfile: Boolean, private val ownerId: Int) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return if (isMyProfile) 3 else 2  // 내 프로필이면 3개, 다른 사람 프로필이면 2개
    }

    override fun createFragment(position: Int): Fragment {
        return if(isMyProfile){
            when(position){
                //ViewPager2 화면 전환 - 내 프로필
                0 -> ProfileChallengeFragment()
                1 -> ProfileCertificationRecordFragment()
                else -> ProfileBadgeFragment()
            }
        }else{
            when(position){
                //ViewPager2 화면 전환 - 다른 사람 프로필
                0 -> ProfileChallengeFragment().apply {
                    arguments = Bundle().apply {
                        putInt("ownerId", ownerId)
                        Log.d("myIdDebug", "FollowVPA: $ownerId")
                    }
                }
                else -> ProfileBadgeFragment()
            }
        }
    }
}