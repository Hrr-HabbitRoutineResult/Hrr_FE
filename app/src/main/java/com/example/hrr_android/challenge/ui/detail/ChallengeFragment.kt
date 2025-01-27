package com.example.hrr_android.challenge.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.R
import com.example.hrr_android.challenge.ui.detail.adapter.CertificationListAdapter
import com.example.hrr_android.databinding.FragmentChallengeBinding
import com.example.hrr_android.databinding.LayoutChallengeButtonBinding


class ChallengeFragment : Fragment(), ChallengeDialogInterface {
    // 뷰 바인딩 정의
    private var _binding: FragmentChallengeBinding? = null
    private val binding get() = _binding!!

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
    }

    // JOIN 다이얼로그에서 '네' 버튼 클릭 시 호출
    override fun onJoinButtonClick() {
        addCertificationViews()
        updateButtonLayout(ChallengeState.JOINED)  // 상태 변경
    }

    // 인증 관련 뷰 동적으로 추가
    private fun addCertificationViews() {
        val container = binding.llChallengeContainer
        val ruleView = container.findViewById<View>(R.id.layout_challenge_rule)
        val ruleIndex = container.indexOfChild(ruleView)

        // 이번 주 인증 완료 뷰 추가
        val weeklyView = layoutInflater.inflate(
            R.layout.layout_challenge_weekly_certification,
            container,
            false
        )
        container.addView(weeklyView, ruleIndex)

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
                    updateButtonLayout(ChallengeState.CERTIFIED)
                }
            }
            // TODO: 개발용 완주 화면 테스트, 추후 삭제
//            ChallengeState.CERTIFIED -> {
//                newButtonLayout.findViewById<Button>(R.id.btn_challenge_countdown)?.setOnClickListener {
//                    showCompleteLayout()
//                }
//            }
            else -> {}
        }
    }

    // 상태를 나타내는 enum class
    enum class ChallengeState {
        INITIAL,    // 챌린지 참가 전, 초기 상태
        JOINED,     // 챌린지 참가 후, 인증 전
        CERTIFIED,   // 인증 완료
        COMPLETED    // 챌린지 완주
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}