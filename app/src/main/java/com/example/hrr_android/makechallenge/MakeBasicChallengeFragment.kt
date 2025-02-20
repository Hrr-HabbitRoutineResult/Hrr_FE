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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.hrr_android.ChallengeViewModel
import com.example.hrr_android.databinding.FragmentMakeChallengeBasicBinding
import com.example.hrr_android.databinding.LayoutMakeChallengeHeaderBinding
import com.example.hrr_android.R
import com.example.hrr_android.challenge.ui.detail.ChallengeFragment
import com.example.hrr_android.access.repository.AuthRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
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
    private lateinit var category: String

    private val challengeViewModel: ChallengeViewModel by viewModels()
    @Inject lateinit var authRepository: AuthRepository

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            binding.ivBasicChallengeProfile.setImageURI(it)
            selectedImageUri = it
            updateApplyButtonState()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // `category` 값을 전달받아 저장
        arguments?.getString(ARG_CATEGORY)?.let {
            category = it
        } ?: throw IllegalArgumentException("Category 값이 전달되지 않았습니다.")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMakeChallengeBasicBinding.inflate(inflater, container, false)

        val headerView = binding.root.findViewById<View>(R.id.layout_make_challenge_basic_header)
        _headerBinding = LayoutMakeChallengeHeaderBinding.bind(headerView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBackButton()
        setupButtonGroups()
        setupTextWatchers()
        setupProfileImageSelection()
        setupApplyButtonClick()
    }

    private fun setupBackButton() {
        headerBinding.btnMakeChallengeBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupButtonGroups() {
        durationButtons = listOf(
            binding.btnBasicDuration1week.apply { tag = "week_1" },
            binding.btnBasicDuration2week.apply { tag = "week_2" },
            binding.btnBasicDuration3week.apply { tag = "week_3" },
            binding.btnBasicDuration1month.apply { tag = "month_1" },
            binding.btnBasicDuration3month.apply { tag = "month_3" },
            binding.btnBasicDuration6month.apply { tag = "month_6" },
            binding.btnBasicDuration1year.apply { tag = "year_1" }
        )

        peopleButtons = listOf(
            binding.btnBasicPeople10.apply { tag = 10 },
            binding.btnBasicPeople20.apply { tag = 20 },
            binding.btnBasicPeople30.apply { tag = 30 },
            binding.btnBasicPeople50.apply { tag = 50 },
            binding.btnBasicPeople100.apply { tag = 100 }
        )

        authButtons = listOf(
            binding.btnBasicAuthmeanPicture.apply { tag = "camera" },
            binding.btnBasicAuthmeanWriting.apply { tag = "text" }
        )

        frequencyButtons = listOf(
            binding.btnBasicFrequencyEveryday.apply { tag = "everyday" },
            binding.btnBasicFrequency2perweek.apply { tag = "week_2" },
            binding.btnBasicFrequency3perweek.apply { tag = "week_3" },
            binding.btnBasicFrequency5perweek.apply { tag = "week_5" },
            binding.btnBasicFrequencyWeekday.apply { tag = "weekdays" },
            binding.btnBasicFrequencyWeekend.apply { tag = "weekends" }
        )


        setupSingleSelection(durationButtons)
        setupSingleSelection(peopleButtons)
        setupSingleSelection(frequencyButtons)
        setupAuthMethodSelection()
    }

    // ✅ 챌린지 개설 API 요청
    private fun makeBasicChallenge() {
        viewLifecycleOwner.lifecycleScope.launch {
            val ownerId = authRepository.getUserId()

            val request = MakeChallengeRequest(
                name = binding.etBasicChallengeName.text.toString(),
                ownerId = ownerId,
                type = "basic",
                description = binding.etBasicChallengeDescription.text.toString(),
                challengeImage = "string", // TODO: 사진을 URI로
                category = category,
                challengeStatus = "open",
                maxParticipants = getSelectedMaxParticipants(),
                verificationType = getSelectedVerificationType() ?: "camera",
                rule = binding.etBasicChallengeRule.text.toString(),
                joinDate = null,
                endDate = null,
                duration = getSelectedDuration(),
                frequencyType = "weeklyCount",
                frequencyValue = getSelectedFrequencyValue(),
                days = null,
                keywords = getKeywords()
            )

            challengeViewModel.makeChallenge(request)
        }

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

    private fun getSelectedMaxParticipants(): Int? =
        peopleButtons.firstOrNull { it.isSelected }?.tag as? Int

    private fun getSelectedDuration(): String? =
        durationButtons.firstOrNull { it.isSelected }?.tag as? String

    private fun getSelectedVerificationType(): String? =
        authButtons.firstOrNull { it.isSelected }?.tag as? String

    private fun getSelectedFrequencyValue(): String? =
        frequencyButtons.firstOrNull { it.isSelected }?.tag as? String

    private fun getKeywords(): List<String> {
        return listOf(
            binding.etBasicKeyword1.text.toString().trim(),
            binding.etBasicKeyword2.text.toString().trim(),
            binding.etBasicKeyword3.text.toString().trim()
        ).filter { it.isNotEmpty() }
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
        val isProfileImageSelected = selectedImageUri != null

        val isEnabled = isNameFilled &&
                isDescriptionFilled && isRuleFilled &&
                isDurationSelected && isPeopleSelected &&
                isAuthSelected && isFrequencySelected &&
                isProfileImageSelected

        binding.btnMakeBasicChallenge.post {
            binding.btnMakeBasicChallenge.isEnabled = isEnabled
            if (isEnabled) {
                binding.btnMakeBasicChallenge.setBackgroundResource(R.drawable.bg_button_activate_10)
                binding.btnMakeBasicChallenge.findViewById<TextView>(R.id.tv_make_basic_challenge_apply)
                    .setTextColor(resources.getColor(R.color.white))
            } else {
                binding.btnMakeBasicChallenge.setBackgroundResource(R.drawable.bg_button_deactivate_10)
                binding.btnMakeBasicChallenge.findViewById<TextView>(R.id.tv_make_basic_challenge_apply)
                    .setTextColor(resources.getColor(R.color.text_tertiary))
            }
            binding.btnMakeBasicChallenge.invalidate()
        }
    }

    // "개설하기" 버튼 클릭 시 ChallengeFragment로 이동하며 다이얼로그 띄우기
    private fun setupApplyButtonClick() {
        binding.btnMakeBasicChallenge.setOnClickListener {
            val challengeFragment = ChallengeFragment()
            makeBasicChallenge()

            val args = Bundle()
            args.putBoolean("showCreateDialog", true)
            challengeFragment.arguments = args

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, challengeFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: String): MakeBasicChallengeFragment {
            return MakeBasicChallengeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATEGORY, category)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _headerBinding = null
    }
}