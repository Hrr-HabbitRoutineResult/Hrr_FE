package com.example.hrr_android.challenge.ui.detail


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.hrr_android.databinding.DialogChallengeJoinBinding

interface ChallengeDialogInterface {
    fun onJoinButtonClick()
}

class ChallengeDialog(
    private val dialogInterface: ChallengeDialogInterface,
) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogChallengeJoinBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogChallengeJoinBinding.inflate(inflater, container, false)

        // 다이얼로그 배경 투명하게 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // '아니오' 버튼 클릭 시 다이얼로그만 닫기
        binding.btnJoinNo.setOnClickListener {
            dismiss()
        }

        // '네' 버튼 클릭 시 참가 처리 후 다이얼로그 닫기
        binding.btnJoinYes.setOnClickListener {
            dialogInterface.onJoinButtonClick()
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}