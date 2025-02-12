package com.example.hrr_android.makechallenge

import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import com.example.hrr_android.databinding.FragmentMakeChallengeBasicBinding
import com.example.hrr_android.databinding.LayoutMakeChallengeHeaderBinding

class MakeBasicChallengeFragment : Fragment() {

    private var _binding: FragmentMakeChallengeBasicBinding? = null
    private val binding get() = _binding!!

    private var _headerBinding: LayoutMakeChallengeHeaderBinding? = null
    private val headerBinding get() = _headerBinding!!

    // 버튼 그룹을 담는 리스트
    private lateinit var durationButtons: List<View>
    private lateinit var peopleButtons: List<View>
    private lateinit var authButtons: List<View>
    private lateinit var frequencyButtons: List<View>

    private var selectedImageUri: Uri? = null

    //갤러리에서 사진 선태 기능
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            binding.ivBasicChallengeProfile.setImageURI(uri)
            selectedImageUri = uri
            updateApplyButtonState()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMakeChallengeBasicBinding.inflate(inflater, container, false)
        _headerBinding = LayoutMakeChallengeHeaderBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뒤로가기 버튼
        headerBinding.btnMakeChallengeBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 버튼 그룹 초기화
        durationButtons = listOf(
            binding.btnBasicDuration1week, binding.btnBasicDuration2week,
            binding.btnBasicDuration3week, binding.btnBasicDuration1month,
            binding.btnBasicDuration3month, binding.btnBasicDuration6month,
            binding.btnBasicDuration1year
        )

        peopleButtons = listOf(
            binding.btnBasicPeople10, binding.btnBasicPeople20,
            binding.btnBasicPeople30, binding.btnBasicPeople50,
            binding.btnBasicPeople100
        )

        authButtons = listOf(
            binding.btnBasicAuthmeanPicture, binding.btnBasicAuthmeanWriting
        )

        frequencyButtons = listOf(
            binding.btnBasicFrequencyEveryday, binding.btnBasicFrequency2perweek,
            binding.btnBasicFrequency3perweek, binding.btnBasicFrequency5perweek,
            binding.btnBasicFrequencyWeekday, binding.btnBasicFrequencyWeekend
        )

        setupSingleSelection(durationButtons)
        setupSingleSelection(peopleButtons)
        setupSingleSelection(frequencyButtons)
        setupAuthMethodSelection()

        setupTextWatchers()

    }

    // 한 그룹에서 하나의 버튼만 선택되도록 설정
    private fun setupSingleSelection(buttons: List<View>) {
        buttons.forEach { button ->
            button.setOnClickListener {
                buttons.forEach { btn ->
                    btn.isSelected = false
                    btn.isActivated = false
                    setButtonTextColor(btn, R.color.text_tertiary)
                }
                button.isSelected = true
                button.isActivated = true
                setButtonTextColor(button, R.color.white) // 선택된 버튼만 흰색 적용
                updateApplyButtonState()
            }
        }
    }

    // 인증 수단 버튼의 글자색 변경 방지
    private fun setupAuthMethodSelection() {
        val authButtons = listOf(
            binding.btnBasicAuthmeanPicture, binding.btnBasicAuthmeanWriting
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

    // 버튼 내의 TextView 색상을 변경
    private fun setButtonTextColor(button: View, colorResId: Int) {
        if (button is ViewGroup) {
            for (i in 0 until button.childCount) {
                val child = button.getChildAt(i)
                if (child is TextView) {
                    child.setTextColor(ContextCompat.getColor(requireContext(), colorResId))
                }
            }
        }
    }

    // EditText 입력 감지
     private fun setupTextWatchers() {
        val editTexts = listOf(
            binding.etBasicChallengeName,
            binding.etBasicChallengeDescription,
            binding.etBasicChallengeRule
        )

        editTexts.forEach { editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    updateApplyButtonState()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    // ✅ 갤러리에서 사진 선택하는 기능 추가
    private fun setupProfileImageSelection() {
        binding.ivBasicChallengeProfile.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }
    }

    // 버튼 활성화 상태 업데이트
    private fun updateApplyButtonState() {
        val isNameFilled = binding.etBasicChallengeName.text.isNotBlank()
        val isDescriptionFilled = binding.etBasicChallengeDescription.text.isNotBlank()
        val isRuleFilled = binding.etBasicChallengeRule.text.isNotBlank()

        val isDurationSelected = durationButtons.any { it.isSelected }
        val isPeopleSelected = peopleButtons.any { it.isSelected }
        val isAuthSelected = authButtons.any { it.isSelected }
        val isFrequencySelected = frequencyButtons.any { it.isSelected }

        val isEnabled = isNameFilled && isDescriptionFilled && isRuleFilled &&
                isDurationSelected && isPeopleSelected && isAuthSelected && isFrequencySelected

        binding.btnMakeBasicChallenge.isEnabled = isEnabled
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _headerBinding = null
    }
}
