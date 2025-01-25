package com.example.hrr_android.onboarding.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hrr_android.onboarding.utils.RadioGroupUtils
import com.example.hrr_android.databinding.FragmentInfoSelectBinding

class InfoSelectFragment : Fragment() {
    private var _binding: FragmentInfoSelectBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInfoSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 성별 버튼 선택 상태 관리
        RadioGroupUtils.setupRadioGroup(binding.radioGroupGender)

        // 연령대 버튼 선택 상태 관리
        RadioGroupUtils.setupRadioGroup(binding.radioGroupAge)

        // 직업 버튼 선택 상태 관리
        RadioGroupUtils.setupRadioGroup(binding.radioGroupJob)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 모든 라디오 그룹이 선택되었는지 판단하는 함수
    fun isValidSelection(): Boolean {
        val isGenderValid = RadioGroupUtils.isRadioGroupValid(binding.radioGroupGender)
        val isAgeValid = RadioGroupUtils.isRadioGroupValid(binding.radioGroupAge)
        val isJobValid = RadioGroupUtils.isRadioGroupValid(binding.radioGroupJob)

        return isGenderValid && isAgeValid && isJobValid
    }
}