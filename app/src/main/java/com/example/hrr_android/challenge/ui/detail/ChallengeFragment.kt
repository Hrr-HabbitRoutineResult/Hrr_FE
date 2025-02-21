package com.example.hrr_android.challenge.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hrr_android.R
import com.example.hrr_android.challenge.model.ChallengeDetail
import com.example.hrr_android.challenge.ui.detail.adapter.CertificationListAdapter
import com.example.hrr_android.challenge.ui.record.ProgressFragment
import com.example.hrr_android.challenge.ui.record.RecordFragment
import com.example.hrr_android.databinding.FragmentChallengeBinding
import com.example.hrr_android.databinding.LayoutChallengeButtonBinding
import com.example.hrr_android.databinding.LayoutChallengeHeaderBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.example.hrr_android.UserResponse
import com.example.hrr_android.challenge.model.WeeklyVerificationResponse
import com.example.hrr_android.databinding.LayoutChallengeProfileBinding

@AndroidEntryPoint
class ChallengeFragment : Fragment(), ChallengeDialogInterface {
    // 뷰 바인딩 정의
    private var _binding: FragmentChallengeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChallengeDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChallengeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonBinding = LayoutChallengeButtonBinding.bind(binding.llChallengeButtons.root)

        // '참가하기' 버튼 클릭 시 JOIN 다이얼로그 표시
        buttonBinding.btnChallengeJoin.setOnClickListener {
            val dialog = ChallengeDialog(this, ChallengeDialog.DialogType.JOIN)
            dialog.isCancelable = false
            dialog.show(parentFragmentManager, "ChallengeDialog")
        }
        // TODO: 개발용 CREATE 다이얼로그 표시 테스트, 추후 삭제
//        buttonBinding.btnChallengeJoin.setOnLongClickListener {
//            val dialog = ChallengeDialog(this, ChallengeDialog.DialogType.CREATE)
//            dialog.isCancelable = false
//            dialog.show(parentFragmentManager, "ChallengeDialog")
//            true
//        }
        val showDialog = arguments?.getBoolean("showCreateDialog", false) ?: false
        if (showDialog) {
            showCreateDialog()
        }

        // 전달받은 challengeId로 데이터 요청
        val challengeId = arguments?.getInt("challenge_id", -1) ?: -1
        if (challengeId != -1) {
            viewModel.fetchChallengeDetail(challengeId)
        }

        // 챌린지 데이터 observe
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.challengeState.collect { result ->
                result?.onSuccess { challenge ->
                    updateUI(challenge)
                    // 챌린지 owner의 프로필 정보 요청
                    viewModel.fetchUserProfile(challenge.ownerId)
                }?.onFailure { e ->
                    Toast.makeText(requireContext(), "챌린지 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 프로필 데이터 observe
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userProfileState.collect { result ->
                result?.onSuccess { userProfile ->
                    updateProfileUI(userProfile)
                }?.onFailure { e ->
                    Toast.makeText(requireContext(), "프로필 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // join API 응답 observe
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.joinState.collect { result ->
                result?.onSuccess { challenge ->
                    addCertificationViews()
                    updateButtonLayout(ChallengeState.JOINED)
                    updateUI(challenge)
                }?.onFailure { e ->
                    if (e.message?.contains("이미 참여 중인 챌린지입니다") == true) {
                        // 이미 참여 중인 경우에도 JOINED 상태로 변경
                        addCertificationViews()
                        updateButtonLayout(ChallengeState.JOINED)
                    }
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        // 다른 곳에서 해당 화면으로 넘어올 때 케이스별 View 전환
        // Todo: api 연동 완료되면 challenge_id 넘겨받아서 해당하는 챌린지 정보 바인딩 필요
        val receivedState = arguments?.getString("state", "initial")
        when(receivedState){
            /*"initial" ->
            "joined" ->
            "certified" -> */
            "completed" -> showCompleteLayout()

        }
    }

    // JOIN 다이얼로그에서 '네' 버튼 클릭 시 호출
    override fun onJoinButtonClick() {
        val challengeId = arguments?.getInt("challenge_id", -1) ?: -1
        if (challengeId != -1) {
            viewModel.joinChallenge(challengeId)
        }
    }

    // 요일 버튼 ID와 요일 문자열 매핑
    private val dayButtonMap = mapOf(
        R.id.btn_sunday to "Sunday",
        R.id.btn_monday to "Monday",
        R.id.btn_tuesday to "Tuesday",
        R.id.btn_wednesday to "Wednesday",
        R.id.btn_thursday to "Thursday",
        R.id.btn_friday to "Friday",
        R.id.btn_saturday to "Saturday"
    )

    // 인증 관련 뷰 동적으로 추가
    private fun addCertificationViews() {
        val container = binding.llChallengeContainer
        val ruleView = container.findViewById<View>(R.id.layout_challenge_rule)
        val ruleIndex = container.indexOfChild(ruleView)

        // 이번 주 인증 완료 뷰 추가 후 API 호출
        val weeklyView = layoutInflater.inflate(
            R.layout.layout_challenge_weekly_certification,
            container,
            false
        )
        container.addView(weeklyView, ruleIndex)

        val challengeId = arguments?.getInt("challenge_id", -1) ?: -1
        if (challengeId != -1) {
            viewModel.fetchWeeklyVerification(challengeId)
        }

        // WeeklyVerification 상태 observe
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.weeklyVerificationState.collect { result ->
                result?.onSuccess { response ->
                    updateWeeklyVerificationUI(weeklyView, response)
                }?.onFailure { e ->
                    Toast.makeText(requireContext(), "주간 인증 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 챌린지 인증 현황 뷰 추가
        val certificationListView = layoutInflater.inflate(
            R.layout.layout_challenge_certification_list,
            container,
            false
        )
        container.addView(certificationListView, ruleIndex + 1)

        // RecyclerView 설정
        certificationListView.findViewById<RecyclerView>(R.id.rv_certification_list).apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = CertificationListAdapter()
        }

        // 챌린지 인증 현황 더보기 버튼 클릭 시 ProgressFragment로 이동
        certificationListView.findViewById<View>(R.id.ll_more)?.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, ProgressFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    // 챌린지 UI 업데이트
    private fun updateUI(challenge: ChallengeDetail) {
        updateHeaderUI(challenge)
        updateRuleUI(challenge)
    }

    // 챌린지 헤더 UI 업데이트
    private fun updateHeaderUI(challenge: ChallengeDetail) {
        // 레이아웃 바인딩 사용
        val headerBinding = LayoutChallengeHeaderBinding.bind(
            binding.llChallengeContainer.getChildAt(0) // 첫 번째 child가 header layout
        )

        with(headerBinding) {
            // 챌린지 이름
            tvChallengeHeaderTitle.text = challenge.name
            // 챌린지 소개
            tvChallengeHeaderDescription.text = challenge.description

            // 참여 인원 (현재/최대)
            tvChallengeHeaderCurrentParticipants.text = challenge.currentParticipants.toString()
            tvChallengeHeaderMaxParticipants.text = challenge.maxParticipants.toString()

            // 챌린지 기간 표시
            val durationText = when {
                challenge.duration.startsWith("month_") -> {
                    val months = challenge.duration.substringAfter("month_")
                    "${months}개월"
                }
                challenge.duration.startsWith("week_") -> {
                    val weeks = challenge.duration.substringAfter("week_")
                    "${weeks}주"
                }
                else -> challenge.duration
            }
            // TODO: 인증 요일 정보 추가되면 레이아웃 분리 후 "요일|기간" 형식으로 표시
            tvChallengeHeaderPeriod.text = durationText

            // 헤더 배경 이미지 설정
            challenge.challengeImage?.let { imageUrl ->
                Glide.with(root)
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.img_challenge_profile_bg) // 로딩 중 표시할 이미지
                    .error(R.drawable.img_challenge_profile_bg)       // 에러 시 표시할 이미지
                    .into(ivChallengeHeaderBackground)
            }

            // 닫기 버튼 클릭 리스너
            btnChallengeHeaderClose.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    // 챌린지 규칙 UI 업데이트
    private fun updateRuleUI(challenge: ChallengeDetail) {
        val ruleLayout = binding.llChallengeContainer.findViewById<View>(R.id.layout_challenge_rule)
        ruleLayout?.findViewById<TextView>(R.id.tv_challenge_rule)?.text = challenge.rule
    }

    private fun updateProfileUI(userProfile: UserResponse) {
        val profileBinding = LayoutChallengeProfileBinding.bind(
            binding.llChallengeContainer.getChildAt(1)
        )

        with(profileBinding) {
            tvChallengeProfileName.text = userProfile.nickname
            tvChallengeProfileLevel.text = userProfile.level

            // TODO: 프로필 상세 화면으로 이동
            root.setOnClickListener {
            }
        }
    }

    // 챌린지 완주 시
    private fun showCompleteLayout() {
        // 기존 컨테이너 뷰 모두 제거
        binding.llChallengeContainer.removeAllViews()
        // 프래그먼트 배경색 변경
        binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_50))

        // 헤더만 다시 호출
        val headerView = layoutInflater.inflate(
            R.layout.layout_challenge_header,
            binding.llChallengeContainer,
            false
        )
        binding.llChallengeContainer.addView(headerView)

        // 챌린지 완주 뷰 추가
        val completeView = layoutInflater.inflate(
            R.layout.layout_challenge_complete,
            binding.llChallengeContainer,
            false
        )
        binding.llChallengeContainer.addView(completeView)

        // 버튼 상태 업데이트
        updateButtonLayout(ChallengeState.COMPLETED)
    }

    // 챌린지 완주 확인 함수
    private fun isChallengeCompleted() {
        // TODO: 챌린지 완주 확인 로직 추가 구현 예정
    }

    // 챌린지 상태에 따른 하단 버튼 영역 업데이트
    private fun updateButtonLayout(state: ChallengeState) {
        val layoutResId = when(state) {
            ChallengeState.INITIAL -> R.layout.layout_challenge_button
            ChallengeState.JOINED -> R.layout.layout_challenge_button_certification
            ChallengeState.CERTIFIED -> R.layout.layout_challenge_button_countdown
            ChallengeState.COMPLETED -> R.layout.layout_challenge_button_complete
        }

        // 현재 버튼 레이아웃
        val currentButtonLayout = binding.root.findViewById<View>(R.id.ll_challenge_buttons)

        // 새로운 버튼 레이아웃 생성
        val newButtonLayout = layoutInflater.inflate(layoutResId, binding.root as ViewGroup, false).apply {
            id = R.id.ll_challenge_buttons
            layoutParams = currentButtonLayout.layoutParams
        }

        // ConstraintLayout에서 뷰 교체
        val parentLayout = binding.root as ViewGroup
        parentLayout.removeView(currentButtonLayout)
        parentLayout.addView(newButtonLayout)

        // 상태별 클릭 리스너 설정
        when(state) {
            ChallengeState.JOINED -> {
                newButtonLayout.findViewById<Button>(R.id.btn_challenge_certification)?.setOnClickListener {
                    // 현재 챌린지 ID를 PhotoCertificationFragment로 전달
                    val challengeId = arguments?.getInt("challenge_id", -1) ?: -1
                    findNavController().navigate(
                        R.id.action_challengeFragment_to_photoCertificationFragment,
                        Bundle().apply {
                            putInt("challenge_id", challengeId)
                        }
                    )
                }
            }
            // TODO: 개발용 완주 화면 테스트, 추후 삭제
//            ChallengeState.CERTIFIED -> {
//                newButtonLayout.findViewById<Button>(R.id.btn_challenge_countdown)?.setOnClickListener {
//                    showCompleteLayout()
//                }
//            }
            // TODO: 개발용 인증 기록 화면 이동 테스트, 추후 수정 필요
            ChallengeState.COMPLETED -> {
                newButtonLayout.findViewById<Button>(R.id.btn_challenge_complete)?.setOnClickListener {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, RecordFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
            else -> {}
        }
    }

    private fun updateWeeklyVerificationUI(weeklyView: View, verification: WeeklyVerificationResponse) {
        dayButtonMap.forEach { (buttonId, dayName) ->
            val dayButton = weeklyView.findViewById<TextView>(buttonId)

            when {
                verification.checked_days.contains(dayName) -> {
                    // 인증 완료된 요일
                    dayButton.apply {
                        setBackgroundResource(R.drawable.bg_button_weekly_completed)
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    }
                }
                verification.need_certified.contains(dayName) -> {
                    // 인증이 필요한 요일
                    dayButton.apply {
                        setBackgroundResource(R.drawable.bg_button_weekly_required)
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.sub_01))
                    }
                }
                else -> {
                    // 기본 상태
                    dayButton.apply {
                        setBackgroundResource(R.drawable.bg_button_weekly_default)
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_500))
                    }
                }
            }
        }
    }

    // 상태를 나타내는 enum class
    enum class ChallengeState {
        INITIAL,    // 챌린지 참가 전, 초기 상태
        JOINED,     // 챌린지 참가 후, 인증 전
        CERTIFIED,   // 인증 완료
        COMPLETED    // 챌린지 완주
    }

    //챌린지 개설 후 다이얼로그
    private fun showCreateDialog() {
        val dialog = ChallengeDialog(this, ChallengeDialog.DialogType.CREATE)
        dialog.show(parentFragmentManager, "ChallengeDialog")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}