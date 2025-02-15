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
import androidx.fragment.app.setFragmentResultListener
import com.example.hrr_android.databinding.FragmentMakeChallengeStudyBinding
import com.example.hrr_android.databinding.LayoutMakeChallengeHeaderBinding
import java.text.SimpleDateFormat
import java.util.*
import com.example.hrr_android.R


class MakeStudyChallengeFragment : Fragment() {

    private var _binding: FragmentMakeChallengeStudyBinding? = null
    private val binding get() = _binding!!

    private var _headerBinding: LayoutMakeChallengeHeaderBinding? = null
    private val headerBinding get() = _headerBinding!!

    private lateinit var peopleButtons: List<View>
    private lateinit var authButtons: List<View>
    private lateinit var dayButtons: List<View>

    private var selectedStartDate: Long? = null
    private var selectedEndDate: Long? = null
    private var selectedImageUri: Uri? = null

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            binding.ivStudyChallengeProfile.setImageURI(it)
            selectedImageUri = it
            updateApplyButtonState()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMakeChallengeStudyBinding.inflate(inflater, container, false)

        val headerView = binding.root.findViewById<View>(R.id.layout_make_challenge_study_header)
        _headerBinding = LayoutMakeChallengeHeaderBinding.bind(headerView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBackButton()
        setupButtonGroups()
        setupTextWatchers()
        setupProfileImageSelection()
        getSelectedDatesFromCalendar()
    }

    private fun setupBackButton() {
        headerBinding.btnMakeChallengeBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupButtonGroups() {
        peopleButtons = listOf(
            binding.btnStudyPeople10, binding.btnStudyPeople20, binding.btnStudyPeople30
        )

        authButtons = listOf(
            binding.btnStudyAuthmeanPicture, binding.btnStudyAuthmeanWriting
        )

        dayButtons = listOf(
            binding.btnStudyWeekMon, binding.btnStudyWeekTues, binding.btnStudyWeekWed,
            binding.btnStudyWeekThu, binding.btnStudyWeekFri, binding.btnStudyWeekSat,
            binding.btnStudyWeekSun
        )

        setupSingleSelection(peopleButtons)
        setupDaySelection()
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

    private fun setupDaySelection() {
        dayButtons.forEach { button ->
            button.setOnClickListener {
                button.isSelected = !button.isSelected
                button.isActivated = button.isSelected
                setButtonTextColor(button, if (button.isSelected) R.color.white else R.color.text_tertiary)
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
            binding.etStudyChallengeName,
            binding.etStudyChallengeDescription,
            binding.etStudyChallengeRule
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
        binding.ivStudyChallengeProfile.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }
    }

    private fun updateApplyButtonState() {
        val isNameEntered = binding.etStudyChallengeName.text.isNotBlank()
        val isDescriptionEntered = binding.etStudyChallengeDescription.text.isNotBlank()
        val isRuleEntered = binding.etStudyChallengeRule.text.isNotBlank()

        val isPeopleSelected = peopleButtons.any { it.isSelected }
        val isAuthSelected = authButtons.any { it.isSelected }
        val isDaySelected = dayButtons.any { it.isSelected }
        val isStartDateSelected = selectedStartDate != null
        val isEndDateSelected = selectedEndDate != null
        val isProfileImageSelected = selectedImageUri != null

        val isEnabled = isNameEntered &&
                isDescriptionEntered && isRuleEntered &&
                isPeopleSelected && isAuthSelected &&
                isDaySelected && isStartDateSelected &&
                isEndDateSelected && isProfileImageSelected

        binding.btnMakeStudyChallenge.post {  //
            binding.btnMakeStudyChallenge.isEnabled = isEnabled
            if (isEnabled) {
                binding.btnMakeStudyChallenge.setBackgroundResource(R.drawable.bg_button_activate_10)
                binding.btnMakeStudyChallenge.findViewById<TextView>(R.id.tv_make_study_challenge_apply)
                    .setTextColor(resources.getColor(R.color.white))
            } else {
                binding.btnMakeStudyChallenge.setBackgroundResource(R.drawable.bg_button_deactivate_10)
                binding.btnMakeStudyChallenge.findViewById<TextView>(R.id.tv_make_study_challenge_apply)
                    .setTextColor(resources.getColor(R.color.text_tertiary))
            }
            binding.btnMakeStudyChallenge.invalidate()
        }
    }

    private fun getSelectedDatesFromCalendar() {
        setFragmentResultListener("calendarSelection") { _, bundle ->
            selectedStartDate = bundle.getLong("startDate", -1)
            selectedEndDate = bundle.getLong("endDate", -1)

            if (selectedStartDate != -1L && selectedEndDate != -1L) {
                val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                binding.tvStudyDurationStart.text = dateFormat.format(Date(selectedStartDate!!))
                binding.tvStudyDurationEnd.text = dateFormat.format(Date(selectedEndDate!!))
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
