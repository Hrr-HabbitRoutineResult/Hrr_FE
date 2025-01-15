package com.example.hrr_android

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProfileVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ProfileChallengeFragment()
            1 -> ProfileCertificationRecordFragment()
            else -> ProfileBadgeFragment()
        }
    }
}