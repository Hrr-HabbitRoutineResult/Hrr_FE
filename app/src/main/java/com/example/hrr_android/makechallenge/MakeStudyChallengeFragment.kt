package com.example.hrr_android.makechallenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.hrr_android.databinding.FragmentMakeChallengeStudyBinding
import com.example.hrr_android.databinding.LayoutMakeChallengeHeaderBinding
import java.text.SimpleDateFormat
import java.util.*


class MakeStudyChallengeFragment : Fragment() {

    private var _binding: FragmentMakeChallengeStudyBinding? = null
    private val binding get() = _binding!!

    private var _headerBinding: LayoutMakeChallengeHeaderBinding? = null
    private val headerBinding get() = _headerBinding!!

    private var selectedStartDate: Long? = null
    private var selectedEndDate: Long? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMakeChallengeStudyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBackButton() //뒤로가기
        setupPeopleSelection()
        setupAuthMethodSelection() //글자색 변경x
        setupDaySelection()
        setupInputListeners()
        setupChallengeDurationSelection()
        getSelectedDatesFromCalendar() //선택한 날짜 받아오기
    }

    private fun setupBackButton() {
        headerBinding.btnMakeChallengeBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupPeopleSelection() {
        val peopleButtons = listOf(
            binding.btnStudyPeople10, binding.btnStudyPeople20, binding.btnStudyPeople30
        )

        peopleButtons.forEach { button ->
            button.setOnClickListener {
                peopleButtons.forEach { btn ->
                    btn.isSelected = false
                    btn.isActivated = false
                    (btn.getChildAt(0) as? TextView)?.setTextColor(resources.getColor(R.color.text_tertiary))
                }
                button.isSelected = true
                button.isActivated = true
                (button.getChildAt(0) as? TextView)?.setTextColor(resources.getColor(R.color.white))
                updateApplyButtonState()
            }
        }
    }

    private fun setupAuthMethodSelection() {
        val authButtons = listOf(
            binding.btnStudyAuthmeanPicture, binding.btnStudyAuthmeanWriting
        )

        authButtons.forEach { button ->
            button.setOnClickListener {
                authButtons.forEach { btn ->
                    btn.isSelected = false
                    btn.isActivated = false
                }
                button.isSelected = true
                button.isActivated = true
                updateApplyButtonState()
            }
        }
    }

    private fun setupDaySelection() {
        val dayButtons = listOf(
            binding.btnStudyWeekMon, binding.btnStudyWeekTues, binding.btnStudyWeekWed,
            binding.btnStudyWeekThu, binding.btnStudyWeekFri, binding.btnStudyWeekSat,
            binding.btnStudyWeekSun
        )

        dayButtons.forEach { button ->
            button.setOnClickListener {
                button.isSelected = !button.isSelected
                button.isActivated = button.isSelected
                (button.getChildAt(0) as? TextView)?.setTextColor(
                    if (button.isSelected) resources.getColor(R.color.white) else resources.getColor(R.color.text_tertiary)
                )
                updateApplyButtonState()
            }
        }
    }

    private fun setupInputListeners() {
        val editTexts = listOf(
            binding.etStudyChallengeName, binding.etStudyChallengeDescription, binding.etStudyChallengeRule
        )

        editTexts.forEach { editText ->
            editText.addTextChangedListener {
                updateApplyButtonState()
            }
        }
    }

    private fun setupChallengeDurationSelection() {
        binding.layoutStudyDuration.setOnClickListener {
            findNavController().navigate(R.id.action_makeStudyChallengeFragment_to_makeChallengeCalendarFragment)
        }
    }

    private fun updateApplyButtonState() {
        val isNameEntered = binding.etStudyChallengeName.text.isNotBlank()
        val isDescriptionEntered = binding.etStudyChallengeDescription.text.isNotBlank()
        val isRuleEntered = binding.etStudyChallengeRule.text.isNotBlank()
        val isPeopleSelected = listOf(
            binding.btnStudyPeople10, binding.btnStudyPeople20, binding.btnStudyPeople30
        ).any { it.isSelected }

        val isAuthSelected = listOf(
            binding.btnStudyAuthmeanPicture, binding.btnStudyAuthmeanWriting
        ).any { it.isSelected }

        val isDaySelected = listOf(
            binding.btnStudyWeekMon, binding.btnStudyWeekTues, binding.btnStudyWeekWed,
            binding.btnStudyWeekThu, binding.btnStudyWeekFri, binding.btnStudyWeekSat,
            binding.btnStudyWeekSun
        ).any { it.isSelected }

        val isStartDateSelected = selectedStartDate != null
        val isEndDateSelected = selectedEndDate != null

        binding.btnMakeBasicChallenge.isEnabled =
            isNameEntered && isDescriptionEntered && isRuleEntered && isPeopleSelected && isAuthSelected && isDaySelected && isStartDateSelected && isEndDateSelected
    }


    private fun getSelectedDatesFromCalendar() {
        setFragmentResultListener("calendarSelection") { _, bundle ->
            selectedStartDate = bundle.getLong("startDate", -1)
            selectedEndDate = bundle.getLong("endDate", -1)

            if (selectedStartDate != -1L && selectedEndDate != -1L) {
                val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())

                binding.tvSelectedStartDate.text = dateFormat.format(Date(selectedStartDate!!))
                binding.tvSelectedEndDate.text = dateFormat.format(Date(selectedEndDate!!))

                updateApplyButtonState()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _headerBinding = null
    }
}