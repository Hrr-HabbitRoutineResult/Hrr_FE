package com.example.hrr_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.hrr_android.databinding.FragmentMakeChallengeBinding
import com.example.hrr_android.databinding.LayoutMakeChallengeHeaderBinding

import com.example.hrr_android.makechallenge.MakeBasicChallengeFragment
import com.example.hrr_android.makechallenge.MakeStudyChallengeFragment

class MakeChallengeFragment : Fragment() {

    private var _binding: FragmentMakeChallengeBinding? = null
    private val binding get() = _binding!!

    private var _headerBinding: LayoutMakeChallengeHeaderBinding? = null
    private val headerBinding get() = _headerBinding!! // 헤더 바인딩 추가

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMakeChallengeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        headerBinding.btnMakeChallengeBack.setOnClickListener {
            parentFragmentManager.popBackStack() // 이전 프래그먼트로 돌아가기
        }

        setupCategorySelection()
        setupTypeSelection()
        setupButtonState()
        setupApplyButtonClick()
    }

    private fun setupCategorySelection() {
        val categoryButtons = listOf(
            binding.btnChooseCategoryExercise,
            binding.btnChooseCategoryStudy,
            binding.btnChooseCategoryEmployment,
            binding.btnChooseCategoryHabit,
            binding.btnChooseCategoryHobby
        )

        categoryButtons.forEach { button ->
            button.setOnClickListener {
                categoryButtons.forEach { btn ->
                    btn.isSelected = false
                    btn.isActivated = false
                    // 모든 자식 TextView의 색상 변경
                    for (i in 0 until btn.childCount) {
                        val textView = btn.getChildAt(i) as? TextView
                        textView?.setTextColor(resources.getColor(R.color.text_tertiary))
                    }
                    btn.refreshDrawableState()
                }
                button.isSelected = true
                button.isActivated = true
                // 선택된 버튼의 모든 자식 TextView 색상 변경
                for (i in 0 until button.childCount) {
                    val textView = button.getChildAt(i) as? TextView
                    textView?.setTextColor(resources.getColor(R.color.white))
                }
                button.refreshDrawableState()
                updateButtonState() // 버튼 상태 업데이트
            }
        }
    }

    private fun setupTypeSelection() {
        val typeButtons = listOf(
            binding.btnChooseTypeStudy,
            binding.btnChooseTypeBasic
        )

        typeButtons.forEach { button ->
            button.setOnClickListener {
                typeButtons.forEach { btn ->
                    btn.isSelected = false
                    btn.isActivated = false
                    // 모든 자식 TextView의 색상 변경
                    for (i in 0 until btn.childCount) {
                        val textView = btn.getChildAt(i) as? TextView
                    }
                    btn.refreshDrawableState()
                }
                button.isSelected = true
                button.isActivated = true
                // 선택된 버튼의 모든 자식 TextView 색상 변경
                for (i in 0 until button.childCount) {
                    val textView = button.getChildAt(i) as? TextView
                    textView?.setTextColor(resources.getColor(R.color.white))
                }
                button.refreshDrawableState()
                updateButtonState() // 버튼 상태 업데이트
            }
        }
    }

    private fun setupButtonState() {
        // 초기 상태에서 btnApply 비활성화
        binding.btnApply.isEnabled = true
        binding.btnApply.refreshDrawableState()
    }

    private fun updateButtonState() {
        // 카테고리와 유형 중 하나라도 선택된 경우 확인
        val isCategorySelected = binding.btnChooseCategoryExercise.isSelected ||
                binding.btnChooseCategoryStudy.isSelected ||
                binding.btnChooseCategoryEmployment.isSelected ||
                binding.btnChooseCategoryHabit.isSelected ||
                binding.btnChooseCategoryHobby.isSelected

        val isTypeSelected = binding.btnChooseTypeStudy.isSelected ||
                binding.btnChooseTypeBasic.isSelected

        // 카테고리와 유형이 모두 선택된 경우에만 btnApply 활성화
        val isEnabled = isCategorySelected && isTypeSelected
        binding.btnApply.isEnabled = isEnabled
        binding.btnApply.refreshDrawableState()
    }

    private fun setupApplyButtonClick() {
        binding.btnApply.setOnClickListener {
            if (binding.btnChooseTypeStudy.isSelected) {
                navigateToFragment(MakeStudyChallengeFragment())
            } else if (binding.btnChooseTypeBasic.isSelected) {
                navigateToFragment(MakeBasicChallengeFragment())
            }
        }
    }

    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(this.id, fragment)
            .addToBackStack(null) // 뒤로 가기 버튼을 눌렀을 때 이전 화면으로 돌아갈 수 있도록 함
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _headerBinding = null
    }
}