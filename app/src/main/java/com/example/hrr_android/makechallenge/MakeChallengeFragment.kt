package com.example.hrr_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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
        val headerView = binding.root.findViewById<View>(R.id.layout_make_challenge_header)
        _headerBinding = LayoutMakeChallengeHeaderBinding.bind(headerView)
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
        val isCategorySelected = listOf(
            binding.btnChooseCategoryExercise,
            binding.btnChooseCategoryStudy,
            binding.btnChooseCategoryEmployment,
            binding.btnChooseCategoryHabit,
            binding.btnChooseCategoryHobby
        ).any { it.isActivated } //

        val isTypeSelected = listOf(
            binding.btnChooseTypeStudy,
            binding.btnChooseTypeBasic
        ).any { it.isActivated } //

        // 카테고리 & 유형이 모두 선택된 경우에만 활성화
        val isEnabled = isCategorySelected && isTypeSelected

        binding.btnApply.post {
            binding.btnApply.isEnabled = isEnabled
            if (isEnabled) {
                binding.btnApply.setBackgroundResource(R.drawable.bg_button_activate_10)
                binding.btnApply.findViewById<TextView>(R.id.tv_make_challenge_apply).setTextColor(resources.getColor(R.color.white))
            } else {
                binding.btnApply.setBackgroundResource(R.drawable.bg_button_deactivate_10)
                binding.btnApply.findViewById<TextView>(R.id.tv_make_challenge_apply).setTextColor(resources.getColor(R.color.text_tertiary))
            }
            binding.btnApply.invalidate()
        }
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
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _headerBinding = null
    }
}