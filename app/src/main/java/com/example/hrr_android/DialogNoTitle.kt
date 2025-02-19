package com.example.hrr_android

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import com.example.hrr_android.databinding.DialogNoTitleBinding

class DialogNoTitle(
    context: Context,
    private val message: String,
    private val yesText: String,
    private val noText: String,
    private val listener: DialogListener
) : Dialog(context) {

    private lateinit var binding: DialogNoTitleBinding

    interface DialogListener {
        fun onYesClicked()
        fun onNoClicked()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰 바인딩 초기화
        binding = DialogNoTitleBinding.inflate(LayoutInflater.from(context))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        // 다이얼로그 UI 설정
        binding.tvDialogNoTitleContent.text = message
        binding.tvDialogNoTitleYes.text = yesText
        binding.tvDialogNoTitleNo.text = noText

        // 네 버튼 클릭 리스너
        binding.tvDialogNoTitleYes.setOnClickListener {
            listener.onYesClicked()
            dismiss()
        }

        // 아니오 버튼 클릭 리스너
        binding.tvDialogNoTitleNo.setOnClickListener {
            listener.onNoClicked()
            dismiss()
        }

        // 다이얼로그 배경 투명하게 설정
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}
