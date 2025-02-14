package com.example.hrr_android

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FollowVPAdapter(fragment: Fragment, private var ownerId: Int = 0) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        Log.d("otherDebug", "FollowVPAdapter - $ownerId")
        return when(position){
            //ViewPager2 화면 전환
            0 -> FollowerFragment()
            1 -> FollowingFragment()
            else -> FollowerFragment()
        }
    }
}