package com.example.hrr_android.makechallenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        startCalendarView.setOnDateChangeListener { selectedDate ->
            binding.viewMakeChallengeCalendar1.tag = selectedDate
        }

        endCalendarView.setOnDateChangeListener { selectedDate ->
            binding.viewMakeChallengeCalendar2.tag = selectedDate
        }
    }

    private fun setupCompleteButton() {
        binding.btnMakeBasicChallenge.setOnClickListener {
            val startDate = binding.viewMakeChallengeCalendar1.tag as? String
            val endDate = binding.viewMakeChallengeCalendar2.tag as? String

            if (startDate != null && endDate != null) {
                // 선택한 날짜를 이전 Fragment로 전달
                parentFragmentManager.popBackStack()
            } else {
                // 날짜를 선택하지 않으면 알림 표시 가능
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _headerBinding = null
    }
}
