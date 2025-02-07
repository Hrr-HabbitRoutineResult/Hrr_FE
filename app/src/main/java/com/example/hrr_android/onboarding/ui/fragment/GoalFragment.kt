package com.example.hrr_android.onboarding.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hrr_android.onboarding.utils.RadioGroupUtils
import com.example.hrr_android.databinding.FragmentGoalBinding

class GoalFragment : Fragment() {
    private var _binding: FragmentGoalBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGoalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 목표 버튼 선택 상태 관리
        RadioGroupUtils.setupRadioGroupWithMax(binding.radioGroupGoals, 3, this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 라디오 그룹이 선택되었는지 판단하는 함수
    fun isValidSelection(): Boolean {
        val isGoalValid = RadioGroupUtils.hasSelectedButtons(binding.radioGroupGoals)
        return isGoalValid
    }
}