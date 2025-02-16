package com.example.hrr_android.makechallenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.hrr_android.R
import com.example.hrr_android.databinding.FragmentMakeChallengeCalendarBinding
import com.example.hrr_android.databinding.LayoutMakeChallengeHeaderBinding

class MakeChallengeCalendarFragment : Fragment() {

    private var _binding: FragmentMakeChallengeCalendarBinding? = null
    private val binding get() = _binding!!

    private var _headerBinding: LayoutMakeChallengeHeaderBinding? = null
    private val headerBinding get() = _headerBinding!!

    private var selectedStartDate: Long? = null
    private var selectedEndDate: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMakeChallengeCalendarBinding.inflate(inflater, container, false)

        val headerView = binding.root.findViewById<View>(R.id.layout_make_challenge_calendar_header)
        _headerBinding = LayoutMakeChallengeHeaderBinding.bind(headerView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyCalendarBackground()

        headerBinding.btnMakeChallengeBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        setupCalendarSelection()
        setupCompleteButton()
    }

    private fun applyCalendarBackground() {
        binding.viewMakeChallengeCalendar1.findViewById<View>(R.id.ll_calendar)?.apply {
            setBackgroundResource(R.drawable.bg_make_challenge_calendar)
        }
        binding.viewMakeChallengeCalendar2.findViewById<View>(R.id.ll_calendar)?.apply {
            setBackgroundResource(R.drawable.bg_make_challenge_calendar)
        }
    }

    // 캘린더에서 날짜 선택 감지 → 즉시 버튼 상태 업데이트
    private fun setupCalendarSelection() {
        binding.viewMakeChallengeCalendar1.setOnDateSelectedListener { selectedDate ->
            selectedStartDate = selectedDate.timeInMillis
            updateCompleteButtonState()
        }

        binding.viewMakeChallengeCalendar2.setOnDateSelectedListener { selectedDate ->
            selectedEndDate = selectedDate.timeInMillis
            updateCompleteButtonState()
        }
    }

    // 완료 버튼 활성화. (종료일이 시작일보다 빠를 시 비활성화)
    private fun updateCompleteButtonState() {
        val isBothDatesSelected = selectedStartDate != null && selectedEndDate != null
        val isEndDateAfterStartDate = selectedStartDate != null && selectedEndDate != null &&
                selectedEndDate!! >= selectedStartDate!!

        val isButtonEnabled = isBothDatesSelected && isEndDateAfterStartDate

        binding.btnDurationSelected.isEnabled = isButtonEnabled
        if (isButtonEnabled) {
            binding.btnDurationSelected.setBackgroundResource(R.drawable.bg_button_activate_10)
            binding.btnDurationSelected.findViewById<TextView>(R.id.tv_calendar_apply)
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        } else {
            binding.btnDurationSelected.setBackgroundResource(R.drawable.bg_button_deactivate_10)
            binding.btnDurationSelected.findViewById<TextView>(R.id.tv_calendar_apply)
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.text_tertiary))
        }
        binding.btnDurationSelected.invalidate()
    }

    // "완료" 버튼 클릭 시 날짜 전달 후 이전 화면으로 이동
    private fun setupCompleteButton() {
        binding.btnDurationSelected.setOnClickListener {
            if (selectedStartDate != null && selectedEndDate != null) {
                setFragmentResult(
                    "calendarSelection",
                    Bundle().apply {
                        putLong("startDate", selectedStartDate!!)
                        putLong("endDate", selectedEndDate!!)
                    }
                )
                parentFragmentManager.popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _headerBinding = null
    }
}
