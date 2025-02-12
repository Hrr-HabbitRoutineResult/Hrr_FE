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
        binding.viewMakeChallengeCalendar1.parent.let { parentView ->
            if (parentView is LinearLayout) {
                parentView.setBackgroundResource(R.drawable.bg_make_challenge_calendar)
                parentView.setPadding(0, 54, 0, 0)
            }
        }

        binding.viewMakeChallengeCalendar2.parent.let { parentView ->
            if (parentView is LinearLayout) {
                parentView.setBackgroundResource(R.drawable.bg_make_challenge_calendar)
                parentView.setPadding(0, 54, 0, 0)
            }
        }

        // 뒤로 가기 버튼
        headerBinding?.btnMakeChallengeBack?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        setupCalendarSelection()
        setupCompleteButton()
    }

    private fun setupCalendarSelection() {
        val startCalendarView = binding.viewMakeChallengeCalendar1
        val endCalendarView = binding.viewMakeChallengeCalendar2

        binding.viewMakeChallengeCalendar1.tag = startCalendarView.getSelectedDate()?.timeInMillis
        binding.viewMakeChallengeCalendar2.tag = endCalendarView.getSelectedDate()?.timeInMillis
    }

    private fun setupCompleteButton() {
        binding.btnMakeBasicChallenge.setOnClickListener {
            val startDate = binding.viewMakeChallengeCalendar1.tag as? Long
            val endDate = binding.viewMakeChallengeCalendar2.tag as? Long

            if (startDate != null && endDate != null) {
                // 선택한 날짜를 이전 Fragment로 전달
                parentFragmentManager.popBackStack()
            }
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
            _headerBinding = null
        }
    }
}