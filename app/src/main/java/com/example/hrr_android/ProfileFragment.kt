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
    private lateinit var binding: FragmentProfileBinding            //뷰 바인딩
    private var tabs = arrayListOf("챌린지", "인증기록", "뱃지")        //탭 제목
    private var selectedBadges = ArrayList<Badge>()                 //대표 뱃지 리스트

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        //ViewPager2 Adapter 연결
        val profileVPAdapter = ProfileVPAdapter(this)
        binding.vpProfile.adapter = profileVPAdapter

        //탭 제목 설정
        TabLayoutMediator(binding.tlProfile, binding.vpProfile){
            tab, position ->tab.text = tabs[position]
        }.attach()

        //뱃지 더미 데이터 - 테스트 시 주석 해제 or 설정
        selectedBadges.apply {
            add(Badge("프로 챌린저", R.drawable.img_badge_challenge_01))
            add(Badge("수준급 스터디언", R.drawable.img_badge_challenge_01))
            add(Badge("운동 스타터", R.drawable.img_badge_challenge_01))
        }

        //클릭 이벤트 처리 설정
        initClickListener()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 레벨 달성률 반원 게이지 바
        val circularProgressBar = binding.cpbProfileLevelGauge

        // 레벨 달성률 수치
        val requiredPoint = 100
        val earnedPoint = 76

        // 퍼센트 계산
        val progressPercentage = (earnedPoint / requiredPoint.toFloat()) * 50       //반원이라 백분율의 절반만 사용

        // CircularProgressBar 설정 ; 그라데이션, 시간
        circularProgressBar.apply {
            progressBarColorDirection =
                CircularProgressBar.GradientDirection.RIGHT_TO_LEFT
            setProgressWithAnimation(progressPercentage, 1000) // 1초 동안
        }

        //설정한 대표 뱃지 개수에 따라 visibility 조정
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

        // 설정 버튼 클릭 처리
        binding.ivProfileMenu.setOnClickListener {
            // Fragment 전환
            this.parentFragmentManager.beginTransaction()
                .replace(R.id.main_frame, SettingFragment())
                .addToBackStack(null)
                .commit()
        }

    }

    private fun initClickListener() {
        binding.llProfileRank.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frame, ProfileLevelFragment())
                .addToBackStack(null) // 뒤로 가기 지원
                .commit()
        }
    }

}