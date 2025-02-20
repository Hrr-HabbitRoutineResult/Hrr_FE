
package com.example.hrr_android.makechallenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hrr_android.ChallengeViewModel
import com.example.hrr_android.R
import com.example.hrr_android.databinding.FragmentMakeChallengeBinding
import com.example.hrr_android.databinding.LayoutMakeChallengeHeaderBinding
import com.example.hrr_android.makechallenge.MakeBasicChallengeFragment
import com.example.hrr_android.makechallenge.MakeStudyChallengeFragment

class MakeChallengeFragment : Fragment() {

    private var _binding: FragmentMakeChallengeBinding? = null
    private val binding get() = _binding!!

    private var _headerBinding: LayoutMakeChallengeHeaderBinding? = null
    private val headerBinding get() = _headerBinding!!

    private var selectedCategory: String? = null  // 선택된 카테고리 저장
    private var selectedType: String? = null  //  선택된 챌린지 타입 저장

    private val challengeViewModel: ChallengeViewModel by viewModels()

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
        val categoryButtons = mapOf(
            binding.btnChooseCategoryExercise to "exercise",
            binding.btnChooseCategoryStudy to "study",
            binding.btnChooseCategoryEmployment to "jobPreparation",
            binding.btnChooseCategoryHabit to "lifestyle",
            binding.btnChooseCategoryHobby to "hobby"
        )

        categoryButtons.forEach { (button, category) ->
            button.setOnClickListener {
                categoryButtons.keys.forEach { btn ->
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
                selectedCategory = category
                updateButtonState() // 버튼 상태 업데이트
            }
        }
    }

    private fun setupTypeSelection() {
        val typeButtons = mapOf(
            binding.btnChooseTypeStudy to "study",
            binding.btnChooseTypeBasic to "basic"
        )

        typeButtons.forEach { (button, type) ->
            button.setOnClickListener {
                typeButtons.keys.forEach { btn ->
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
                selectedType = type
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
        val isCategorySelected = selectedCategory != null
        val isTypeSelected = selectedType != null

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
            if (selectedCategory == null || selectedType == null) return@setOnClickListener

            val fragment = when (selectedType) {
                "study" -> MakeStudyChallengeFragment.newInstance(selectedCategory!!)
                "basic" -> MakeBasicChallengeFragment.newInstance(selectedCategory!!)
                else -> return@setOnClickListener
            }

            navigateToFragment(fragment)
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
