package com.example.hrr_android

import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.hrr_android.databinding.DialogLevelGetBinding

interface LevelDialogInterface {
    fun onGetButtonClick()
}

class LevelDialog() : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogLevelGetBinding? = null
    private val binding get() = _binding!!

    //interface 전달 받기
    private var dialogInterface: LevelDialogInterface? = null

    fun setListener(dialogInterface: LevelDialogInterface) {
        this.dialogInterface = dialogInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogLevelGetBinding.inflate(inflater, container, false)

        // 전달받은 리소스 ID로 다이얼로그 뷰 세팅
        val drawableResId = arguments?.getInt("drawableResId") ?: R.drawable.bg_level_map_default

        setupDialogActions(drawableResId)

        // 다이얼로그 배경 투명하게 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 'X' 버튼 클릭 시 창 닫기
        binding.ivLevelClose.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupDialogActions(drawableResId: Int) {
        when (drawableResId) {
            //미달성 레벨
            R.drawable.bg_level_map_default -> {
                applyBlackWhiteFilter(binding.ivLevelDialogBg)  //배경 흑백 처리
                //"획득" 버튼 색상 변경
                binding.btnLevelGet.apply {
                    setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.bg_radius_30_grey_50))
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))
                }


            }
            //최초 달성 레벨
            R.drawable.bg_level_map_achieved_first -> {
                //"획득" 버튼 클릭 처
                binding.btnLevelGet.setOnClickListener {
                    dialogInterface?.onGetButtonClick()
                    dismiss()
                }
            }
            //달성 완료 레벨
            R.drawable.bg_level_map_achieved -> {
                binding.btnLevelGet.visibility = View.INVISIBLE //"획득" 버튼 숨김 처리
            }
        }
    }

    private fun applyBlackWhiteFilter(imageView: ImageView) {
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f) // 흑백 효과 적용

        val filter = ColorMatrixColorFilter(colorMatrix)
        imageView.colorFilter = filter
    }

    companion object{
        fun newInstance(drawableResId: Int): LevelDialog{
            val dialog = LevelDialog()
            val args = Bundle()
            args.putInt("drawableResId", drawableResId)
            dialog.arguments = args
            return dialog
        }
    }
}