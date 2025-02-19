package com.example.hrr_android.challenge.ui.detail


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.hrr_android.databinding.DialogChallengeBinding

interface ChallengeDialogInterface {
    fun onJoinButtonClick()
}

class ChallengeDialog(
    private val dialogInterface: ChallengeDialogInterface,
    private val type: DialogType // 다이얼로그 타입을 지정하여 다이얼로그 내용 분기 처리
) : DialogFragment() {

    // 다이얼로그 타입 구분을 위한 enum 정의
    enum class DialogType {
        JOIN,      // 챌린지 참가 시 다이얼로그
        CREATE     // 챌린지 개설 시 다이얼로그
    }

    // 뷰 바인딩 정의
    private var _binding: DialogChallengeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogChallengeBinding.inflate(inflater, container, false)

        // 다이얼로그 배경 투명하게 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 다이얼로그 타입에 따른 UI 설정
        setupDialogContent()

        return binding.root
    }

    // 다이얼로그 타입에 따라 setup 함수 호출
    private fun setupDialogContent() {
        when (type) {
            DialogType.JOIN -> setupJoinDialog()
            DialogType.CREATE -> setupCreateDialog()
        }
    }

    // 챌린지 참가 시 다이얼로그 UI 설정
    private fun setupJoinDialog() {
        binding.apply {
            btnJoinYes.setOnClickListener {
                dialogInterface.onJoinButtonClick()
                dismiss()
            }
            btnJoinNo.setOnClickListener {
                dismiss()
            }
        }
    }

    // 챌린지 개설 시 다이얼로그 UI 설정
    private fun setupCreateDialog() {
        binding.apply {
            tvChallengeDialogTitle.text = "챌린지가 개설되었습니다!"
            tvChallengeDialogDescription.text = "챌린지에 참가할 다른 챌린저를 위해\n" +
                    "예시가 될 인증글을 업로드해주세요"
            btnJoinNo.visibility = View.GONE // '아니오' 버튼 숨기기
            btnJoinYes.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}