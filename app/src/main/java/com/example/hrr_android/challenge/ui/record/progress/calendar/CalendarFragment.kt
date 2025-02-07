package com.example.hrr_android.challenge.ui.record.progress.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hrr_android.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {
    // 뷰 바인딩 정의
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setupListeners()
    }

    // 뷰 초기화
    private fun initViews() {
        // 헤더 영역 타이틀 변경
        binding.layoutRecordHeader.tvRecordTitle.text = "달력"
        // 버튼 텍스트 변경
        binding.layoutChallengeButtonComplete.btnChallengeComplete.text = "적용하기"
    }

    // 클릭 이벤트 설정
    private fun setupListeners() {
        // 뒤로가기 버튼 클릭 이벤트
        binding.layoutRecordHeader.btnRecordBack.setOnClickListener {
            handleBackPressed()
        }

        // 적용하기 버튼 클릭 이벤트
        binding.layoutChallengeButtonComplete.btnChallengeComplete.setOnClickListener {
            handleDateSelection()
        }

        // 새로고침 버튼 클릭 이벤트
        binding.btnCalendarRefresh.setOnClickListener {
            handleRefresh()
        }
    }

    // 뒤로가기 동작 처리
    private fun handleBackPressed() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    // 날짜 선택 처리
    private fun handleDateSelection() {
        val selectedDate = binding.viewCalendar.getSelectedDate()
        // TODO: 선택된 날짜 처리 로직 추가
    }

    // 새로고침 처리
    private fun handleRefresh() {
        // TODO: 새로고침 처리 로직 추가
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}