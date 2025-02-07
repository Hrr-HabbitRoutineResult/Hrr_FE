package com.example.hrr_android.challenge.ui.record

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.hrr_android.challenge.ui.record.progress.adapter.ProgressViewPagerAdapter
import com.example.hrr_android.databinding.FragmentProgressBinding
import com.google.android.material.tabs.TabLayoutMediator


class ProgressFragment : Fragment() {
    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 헤더 영역 타이틀 변경
        binding.layoutRecordHeader.tvRecordTitle.text = "인증 현황"

        // ViewPager2 설정
        val viewPager = binding.vpProgress
        val adapter = ProgressViewPagerAdapter(this)
        viewPager.adapter = adapter

        // TabLayout과 ViewPager2 연결
        TabLayoutMediator(binding.tlProgress, binding.vpProgress) { tab, position ->
            tab.text = when (position) {
                0 -> "마이"
                1 -> "챌린저"
                else -> null
            }
        }.attach()

        // 기본 탭을 '챌린저'로 설정
        viewPager.setCurrentItem(1, false)

        // 뒤로가기 버튼 클릭 이벤트
        binding.layoutRecordHeader.btnRecordBack.setOnClickListener {
            handleBackPressed()
        }
    }

    // 시스템 뒤로가기 설정
    private fun setupBackPressed() {
        // 휴대폰 자체의 뒤로가기 동작
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    handleBackPressed()
                }
            }
        )
    }

    // 뒤로가기 동작 처리
    private fun handleBackPressed() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}