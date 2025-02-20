package com.example.hrr_android.challengelist

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.hrr_android.R
import com.example.hrr_android.databinding.DialogChallengeFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogChallengeFilterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogChallengeFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ "전체" 기본 선택 + selected=true 상태 유지
        setupDefaultSelection()

        // 필터 비활성화 로직 설정
        setupFilterDisabling()

        // X 버튼 클릭 시 다이얼로그 닫기
        binding.ivCloseFilter.setOnClickListener { dismiss() }

        // ✅ 초기화 버튼 누르면 "전체" 버튼만 선택 상태로 초기화
        binding.btnInitialize.setOnClickListener { setupDefaultSelection() }

        // 적용 버튼 클릭 시 필터 적용
        binding.btnApply.setOnClickListener { applyFilters() }

        // ✅ 필터 버튼 클릭 이벤트 설정 (RadioGroupUtils 사용)
        RadioGroupUtils.setupRadioGroup(binding.radioGroupCategory, this)
        RadioGroupUtils.setupRadioGroup(binding.radioGroupType, this)
        RadioGroupUtils.setupRadioGroup(binding.radioGroupDuration, this)
        RadioGroupUtils.setupRadioGroup(binding.radioGroupFrequency, this)
        RadioGroupUtils.setupRadioGroup(binding.radioGroupWeek, this)
        RadioGroupUtils.setupRadioGroup(binding.radioGroupPeople, this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.apply {
                background = null
                setBackgroundResource(R.drawable.bg_dialog_roundtop)
            }
        }
        return dialog
    }

    private fun setupDefaultSelection() {
        val defaultSelections = mapOf(
            binding.radioGroupCategory to R.id.btn_category_all,
            binding.radioGroupType to R.id.btn_type_all,
            binding.radioGroupDuration to R.id.btn_duration_all,
            binding.radioGroupFrequency to R.id.btn_frequency_all,
            binding.radioGroupWeek to R.id.btn_week_all,
            binding.radioGroupPeople to R.id.btn_people_all
        )

        for ((group, defaultId) in defaultSelections) {
            // ✅ 기존 선택된 버튼 해제 (체크 해제 & selected=false)
            for (i in 0 until group.childCount) {
                val button = group.getChildAt(i) as? RadioButton
                button?.apply {
                    isChecked = false
                    isSelected = false
                }
            }

            // ✅ UI 업데이트 보장: post {} 블록으로 실행 (반영이 안 되면 강제로 반영)
            group.post {
                val defaultButton = group.findViewById<RadioButton>(defaultId)
                defaultButton?.apply {
                    isChecked = true
                    isSelected = true
                }
                group.check(defaultId)
            }
        }
    }

    private fun setupFilterDisabling() {
        binding.radioGroupType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.btn_type_basic -> disableFilter(binding.radioGroupWeek)
                R.id.btn_type_study -> disableFilter(binding.radioGroupFrequency)
                R.id.btn_type_all -> enableFilters(listOf(binding.radioGroupWeek, binding.radioGroupFrequency))
            }
        }

        binding.radioGroupWeek.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId != R.id.btn_week_all) {
                disableFilter(binding.radioGroupFrequency)
            } else {
                enableFilters(listOf(binding.radioGroupFrequency))
            }
        }

        binding.radioGroupFrequency.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId != R.id.btn_frequency_all) {
                disableFilter(binding.radioGroupWeek)
            } else {
                enableFilters(listOf(binding.radioGroupWeek))
            }
        }
    }

    private fun disableFilter(group: RadioGroup) {
        for (i in 0 until group.childCount) {
            val button = group.getChildAt(i) as? RadioButton
            button?.apply {
                isEnabled = false
                isChecked = false
                isSelected = false
                setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_400))
            }
        }
        group.clearCheck() // ✅ 체크된 버튼 초기화
    }

    private fun enableFilters(groups: List<RadioGroup>) {
        for (group in groups) {
            for (i in 0 until group.childCount) {
                val button = group.getChildAt(i) as? RadioButton
                button?.apply {
                    isEnabled = true
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.text_tertiary))
                }
            }
        }
    }

    private fun applyFilters() {
        val selectedFilters = mutableMapOf<String, String>()

        val selectedCategory = getSelectedButtonText(binding.radioGroupCategory)
        if (selectedCategory != "전체") selectedFilters["category"] = selectedCategory

        val selectedType = getSelectedButtonText(binding.radioGroupType)
        if (selectedType != "전체") selectedFilters["type"] = selectedType

        val selectedDuration = getSelectedButtonText(binding.radioGroupDuration)
        if (selectedDuration != "전체") selectedFilters["duration"] = selectedDuration

        val selectedFrequency = getSelectedButtonText(binding.radioGroupFrequency)
        if (selectedFrequency != "전체") selectedFilters["frequency"] = selectedFrequency

        val selectedWeek = getSelectedButtonText(binding.radioGroupWeek)
        if (selectedWeek != "전체") selectedFilters["week"] = selectedWeek

        val selectedPeople = getSelectedButtonText(binding.radioGroupPeople)
        if (selectedPeople != "전체") selectedFilters["people"] = selectedPeople

        val bundle = Bundle().apply {
            selectedFilters.forEach { (key, value) -> putString(key, value) }
        }
        parentFragmentManager.setFragmentResult("filterResult", bundle)

        dismiss()
    }

    private fun getSelectedButtonText(radioGroup: RadioGroup): String {
        val selectedId = radioGroup.checkedRadioButtonId
        return if (selectedId != -1) {
            view?.findViewById<RadioButton>(selectedId)?.text.toString()
        } else {
            "전체"
        }
    }

    private fun updateButtonState(group: RadioGroup, selectedButton: RadioButton) {
        for (i in 0 until group.childCount) {
            val button = group.getChildAt(i) as? RadioButton
            button?.apply {
                isSelected = (this == selectedButton)
                setTextColor(
                    if (isSelected) ContextCompat.getColor(requireContext(), R.color.white)
                    else ContextCompat.getColor(requireContext(), R.color.text_tertiary)
                )
            }
        }
    }
}
