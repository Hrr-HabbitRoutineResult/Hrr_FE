package com.example.hrr_android.makechallenge

import android.content.Context
import android.os.Bundle
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.hrr_android.ChallengeRepository
import com.example.hrr_android.ChallengeViewModel
import com.example.hrr_android.databinding.FragmentMakeChallengeStudyBinding
import com.example.hrr_android.databinding.LayoutMakeChallengeHeaderBinding
import java.text.SimpleDateFormat
import java.util.*
import com.example.hrr_android.R
import com.example.hrr_android.challenge.ui.detail.ChallengeFragment
import com.example.hrr_android.makechallenge.MakeChallengeCalendarFragment
import com.example.hrr_android.access.repository.AuthRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint

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
    private lateinit var category: String

    private val challengeViewModel: ChallengeViewModel by viewModels()
    @Inject lateinit var authRepository: AuthRepository
    @Inject lateinit var challengeRepository: ChallengeRepository

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            binding.ivStudyChallengeProfile.setImageURI(it)
            selectedImageUri = it
            updateApplyButtonState()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 `category` 값을 전달받아 저장
        arguments?.getString(ARG_CATEGORY)?.let {
            category = it
        } ?: throw IllegalArgumentException("Category 값이 전달되지 않았습니다.")
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
        setupCalendarClick()
        getSelectedDatesFromCalendar()
        setupApplyButtonClick()
    }

    //  챌린지 개설 API 요청
    private fun makeStudyChallenge() {
        viewLifecycleOwner.lifecycleScope.launch {
            val ownerId = authRepository.getUserId()

            val request = MakeChallengeRequest(
                name = binding.etStudyChallengeName.text.toString(),
                ownerId = ownerId,
                type = "study",
                description = binding.etStudyChallengeDescription.text.toString(),
                challengeImage = "string", // TODO: 사진을 URI로
                category = category,
                challengeStatus = "open",
                maxParticipants = getSelectedMaxParticipants() ?: 10,
                verificationType = getSelectedVerificationType(),
                rule = binding.etStudyChallengeRule.text.toString(),
                joinDate = selectedStartDate?.let { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it)) },
                endDate = selectedEndDate?.let { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it)) },
                duration = null,
                frequencyType = "specificDays",
                frequencyValue = null,
                days = getSelectedDays() ?: emptyList(),
                keywords = getKeywords()
            )

            Log.d("MakeChallengeDebug", "🔵 챌린지 생성 요청 시작")

            val result = challengeRepository.makeChallenge(request)

            result.onSuccess {
                val challengeId = it.result.challenge.id
                Log.d("MakeChallengeDebug", "🟢 챌린지 생성 성공! ID: $challengeId")

                // ✅ ChallengeRepository를 사용하여 challengeId 저장
                challengeRepository.saveChallengeId(challengeId) // `getChallengeId(challengeId)` 대신 `saveChallengeId(challengeId)` 호출
                Log.d("MakeChallengeDebug", "💾 challengeId 저장 완료: $challengeId")

                navigateToChallengeFragment(challengeId) //  저장된 challengeId를 사용
            }.onFailure { e ->
                Log.e("MakeChallengeDebug", "챌린지 생성 실패: ${e.message}")
            }

        }
    }

    private fun getSelectedMaxParticipants(): Int? {
        return when {
            binding.btnStudyPeople10.isSelected -> 10
            binding.btnStudyPeople20.isSelected -> 20
            binding.btnStudyPeople30.isSelected -> 30
            else -> null
        }
    }

    private fun getSelectedVerificationType(): String {
        return when {
            binding.btnStudyAuthmeanPicture.isSelected -> "camera"
            binding.btnStudyAuthmeanWriting.isSelected -> "text" // "writing" → "text"로 변경
            else -> "text" // 기본값 설정
        }
    }

    private fun getSelectedDays(): List<String>? {
        val selectedDays = listOf(
            binding.btnStudyWeekMon to "월",
            binding.btnStudyWeekTues to "화",
            binding.btnStudyWeekWed to "수",
            binding.btnStudyWeekThu to "목",
            binding.btnStudyWeekFri to "금",
            binding.btnStudyWeekSat to "토",
            binding.btnStudyWeekSun to "일"
        ).filter { it.first.isSelected } //
            .map { it.second }

        return if (selectedDays.isNotEmpty()) selectedDays else null
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

    //기간 클릭시 MakeChallengeCalendarFragment로 이동
    private fun setupCalendarClick() {
        binding.llStudyDuration.setOnClickListener {
            val calendarFragment = MakeChallengeCalendarFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, calendarFragment)
                .addToBackStack(null)
                .commit()
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

    private fun getKeywords(): List<String> {
        return listOf(
            binding.etStudyKeyword1.text.toString().trim(),
            binding.etStudyKeyword2.text.toString().trim(),
            binding.etStudyKeyword3.text.toString().trim()
        ).filter { it.isNotEmpty() }
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

    // "개설하기" 버튼 클릭 시 ChallengeFragment로 이동하며 다이얼로그 띄우기
    private fun setupApplyButtonClick() {
        binding.btnMakeStudyChallenge.setOnClickListener {
            makeStudyChallenge()
        }
    }

    private fun navigateToChallengeFragment(challengeId: Int) {
        Log.d("MakeChallengeDebug", "ChallengeFragment로 이동 준비, challengeId: $challengeId")

        if (!isAdded || view == null) {
            Log.e("MakeChallengeDebug", "Fragment가 더 이상 존재하지 않음. Navigation 중단")
            return
        }

        if (challengeId == -1) {
            Log.e("MakeChallengeDebug", "Challenge ID가 유효하지 않음 (challengeId == -1)")
            return
        }

        try {
            val challengeFragment = ChallengeFragment().apply {
                arguments = Bundle().apply {
                    putInt("challenge_id", challengeId)
                    putBoolean("showCreateDialog", true)
                }
            }

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, challengeFragment)  // main_frame은 실제 Fragment를 표시할 컨테이너 ID
                .addToBackStack(null)  // 뒤로 가기 가능하게 설정
                .commit()


            Log.d("MakeChallengeDebug", "ChallengeFragment로 정상 이동 완료")
        } catch (e: Exception) {
            Log.e("MakeChallengeDebug", "ChallengeFragment 이동 실패: ${e.message}", e)
        }
    }


    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: String): MakeStudyChallengeFragment {
            return MakeStudyChallengeFragment().apply {
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