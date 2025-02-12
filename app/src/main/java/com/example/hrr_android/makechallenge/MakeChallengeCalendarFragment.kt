package com.example.hrr_android.makechallenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.hrr_android.databinding.FragmentMakeChallengeCalendarBinding
import com.example.hrr_android.databinding.LayoutMakeChallengeHeaderBinding
import com.example.hrr_android.challenge.ui.record.progress.calendar.CustomCalendarView
import com.example.hrr_android.R


class MakeChallengeCalendarFragment : Fragment() {

    private var _binding: FragmentMakeChallengeCalendarBinding? = null
    private val binding get() = _binding!!

    private var _headerBinding: LayoutMakeChallengeHeaderBinding? = null
    private val headerBinding get() = _headerBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMakeChallengeCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //  캘린더 배경 추가
        applyCalendarBackground(binding.viewMakeChallengeCalendar1)
        applyCalendarBackground(binding.viewMakeChallengeCalendar2)

        // 뒤로 가기 버튼
        headerBinding?.btnMakeChallengeBack?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        setupCalendarSelection()
        setupCompleteButton()
    }

    private fun applyCalendarBackground(calendarView: CustomCalendarView) {
        calendarView.parent.let { parentView ->
            if (parentView is LinearLayout) {
                parentView.setBackgroundResource(R.drawable.bg_make_challenge_calendar)
                parentView.setPadding(0, 54, 0, 0)
            }
        }
    }

    private fun setupCalendarSelection() {
        val startCalendarView = binding.viewMakeChallengeCalendar1
        val endCalendarView = binding.viewMakeChallengeCalendar2

        binding.viewMakeChallengeCalendar1.setOnClickListener {
            val selectedStartDate = startCalendarView.getSelectedDate()
            if (selectedStartDate != null) {
                binding.viewMakeChallengeCalendar1.tag = selectedStartDate.timeInMillis
            }
        }

        binding.viewMakeChallengeCalendar2.setOnClickListener {
            val selectedEndDate = endCalendarView.getSelectedDate()
            if (selectedEndDate != null) {
                binding.viewMakeChallengeCalendar2.tag = selectedEndDate.timeInMillis
            }
        }
    }

    // 완료 버튼 클릭 시 선택한 날짜를 전달
    private fun setupCompleteButton() {
        binding.btnDurationSelected.setOnClickListener {
            val startDate = (binding.viewMakeChallengeCalendar1.getSelectedDate()?.timeInMillis)
            val endDate = (binding.viewMakeChallengeCalendar2.getSelectedDate()?.timeInMillis)

            if (startDate != null && endDate != null) {
                parentFragmentManager.setFragmentResult(
                    "calendarSelection",
                    Bundle().apply {
                        putLong("startDate", startDate)
                        putLong("endDate", endDate)
                    }
                )
                parentFragmentManager.popBackStack() // 이전 화면으로 돌아가기
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _headerBinding = null
    }
}
