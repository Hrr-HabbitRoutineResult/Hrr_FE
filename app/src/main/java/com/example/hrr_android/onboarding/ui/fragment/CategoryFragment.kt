package com.example.hrr_android.onboarding.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hrr_android.R
import com.example.hrr_android.onboarding.utils.RadioGroupUtils
import com.example.hrr_android.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 카테고리 버튼 선택 상태 관리
        RadioGroupUtils.setupRadioGroup(binding.radioGroupCategory, this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 라디오 그룹이 선택되었는지 판단하는 함수
    fun isValidSelection(): Boolean {
        val isCategoryValid = RadioGroupUtils.isRadioGroupValid(binding.radioGroupCategory)
        return isCategoryValid
    }

    fun getSelectedCategory(): String? {
        val selectedButton = RadioGroupUtils.getSelectedRadioButton(binding.radioGroupCategory)

        return when (selectedButton?.id) {
            R.id.radio_category_health -> "exercise"
            R.id.radio_category_learn -> "study"
            R.id.radio_category_hobby -> "hobby"
            R.id.radio_category_company -> "jobPreparation"
            R.id.radio_category_routine -> "lifestyle"
            else -> null
        }
    }

}