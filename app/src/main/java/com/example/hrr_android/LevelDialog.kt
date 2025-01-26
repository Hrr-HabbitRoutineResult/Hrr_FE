package com.example.hrr_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.hrr_android.databinding.DialogLevelGetBinding

interface LevelDialogInterface {
    fun onGetButtonClick()
}

class ChallengeDialog(
    private val dialogInterface: LevelDialogInterface,
) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogLevelGetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogLevelGetBinding.inflate(inflater, container, false)

        // 다이얼로그 배경 투명하게 설정
        //dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 'X' 버튼 클릭 시 창 닫기
        binding.ivLevelClose.setOnClickListener {
            dismiss()
        }

        // '획득' 버튼 클릭 시 획득 처리 후 다이얼로그 닫기
        binding.btnLevelGet.setOnClickListener {
            dialogInterface.onGetButtonClick()
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}