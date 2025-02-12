package com.example.hrr_android.makechallenge

import android.os.Bundle
import android.net.Uri
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

    private lateinit var durationButtons: List<View>
    private lateinit var peopleButtons: List<View>
    private lateinit var authButtons: List<View>
    private lateinit var frequencyButtons: List<View>

    private var selectedImageUri: Uri? = null

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            binding.ivBasicChallengeProfile.setImageURI(it)
            selectedImageUri = it
            updateApplyButtonState()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMakeChallengeBasicBinding.inflate(inflater, container, false)
        _headerBinding = LayoutMakeChallengeHeaderBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBackButton()
        setupButtonGroups()
        setupTextWatchers()
        setupProfileImageSelection()
    }

    private fun setupBackButton() {
        headerBinding.btnMakeChallengeBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupButtonGroups() {
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
    }

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
                setButtonTextColor(button, R.color.white)
                updateApplyButtonState()
            }
        }
    }

    private fun setupAuthMethodSelection() {
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

    private fun setupProfileImageSelection() {
        binding.ivBasicChallengeProfile.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }
    }

    private fun updateApplyButtonState() {
        val isNameFilled = binding.etBasicChallengeName.text.isNotBlank()
        val isDescriptionFilled = binding.etBasicChallengeDescription.text.isNotBlank()
        val isRuleFilled = binding.etBasicChallengeRule.text.isNotBlank()

        val isDurationSelected = durationButtons.any { it.isSelected }
        val isPeopleSelected = peopleButtons.any { it.isSelected }
        val isAuthSelected = authButtons.any { it.isSelected }
        val isFrequencySelected = frequencyButtons.any { it.isSelected }

        binding.btnMakeBasicChallenge.isEnabled = isNameFilled &&
                isDescriptionFilled && isRuleFilled &&
                isDurationSelected && isPeopleSelected &&
                isAuthSelected && isFrequencySelected
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _headerBinding = null
    }
}
