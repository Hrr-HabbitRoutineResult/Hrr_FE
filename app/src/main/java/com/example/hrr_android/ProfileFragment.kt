package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hrr_android.databinding.FragmentProfileBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private var tabs = arrayListOf("챌린지", "인증기록", "뱃지")
    private var selectedBadges = ArrayList<Badge>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        val profileVPAdapter = ProfileVPAdapter(this)
        binding.vpProfile.adapter = profileVPAdapter

        //탭 제목 설정
        TabLayoutMediator(binding.tlProfile, binding.vpProfile){
            tab, position ->tab.text = tabs[position]
        }.attach()

        //뱃지 더미 데이터
        selectedBadges.apply {
            add(Badge("오늘부터 챌린저", R.drawable.img_badge_challenge_01))
            add(Badge("길어지면 어떻게 될까요", R.drawable.img_badge_challenge_01))
            add(Badge("테스트", R.drawable.img_badge_challenge_01))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val circularProgressBar = binding.cpbProfileLevelGauge

        // 레벨 달성률 수치
        val requiredPoint = 100
        val earnedPoint = 76

        // 퍼센트 계산
        val progressPercentage = (earnedPoint / requiredPoint.toFloat()) * 50

        // CircularProgressBar 설정 ; 그라데이션, 시간
        circularProgressBar.apply {
            progressBarColorDirection =
                CircularProgressBar.GradientDirection.RIGHT_TO_LEFT
            setProgressWithAnimation(progressPercentage, 1000) // 1초 동안
        }

        when(selectedBadges.size){
            0 -> {}
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

}