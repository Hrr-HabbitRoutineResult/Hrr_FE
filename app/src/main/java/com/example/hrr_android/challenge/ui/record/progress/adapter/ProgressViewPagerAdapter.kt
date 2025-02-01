package com.example.hrr_android.challenge.ui.record.progress.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hrr_android.challenge.ui.record.progress.ChallengersProgressFragment
import com.example.hrr_android.challenge.ui.record.progress.MyProgressFragment

class ProgressViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyProgressFragment()
            1 -> ChallengersProgressFragment()
            else -> MyProgressFragment()
        }
    }
}