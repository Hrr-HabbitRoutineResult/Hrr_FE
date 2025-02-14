package com.example.hrr_android

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FollowVPAdapter(fragment: Fragment, private var ownerId: Int = 0, private var myId: Int = 0) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        Log.d("otherDebug", "FollowVPAdapter - $ownerId")
        return when(position){
            //ViewPager2 화면 전환
            0 -> FollowerFragment().apply {
                arguments = Bundle().apply {
                    putInt("ownerId", ownerId)
                    putInt("myId", myId)
                    Log.d("myIdDebug", "FollowVPA: $myId")
                }
            }
            1 -> FollowingFragment().apply {
                arguments = Bundle().apply {
                    putInt("myId", myId)
                }
            }
            else -> FollowerFragment()
        }
    }
}