package com.example.hrr_android.access.ui.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hrr_android.access.ui.LoginActivity
import com.example.hrr_android.databinding.FragmentTemporaryPasswordBinding
import com.google.android.material.snackbar.Snackbar

class TemporaryPasswordFragment : Fragment() {

    private var _binding: FragmentTemporaryPasswordBinding? = null
    private val binding get() = _binding!!

    // 복사가 완료되었는지 상태를 저장하는 변수
    private var isCopied = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTemporaryPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCopyButton()
        setupNextButton()
    }

    // 임시 비밀번호 복사
    private fun setupCopyButton() {
        binding.btnTemporaryPasswordCopy.setOnClickListener {
            val textToCopy = binding.tvTemporaryPassword.text.toString()
            copyToClipboard(textToCopy)
            isCopied = true // 복사가 완료되었음을 기록
        }
    }

    private fun setupNextButton() {
        binding.btnTemporaryPasswordNext.setOnClickListener {
            if (isCopied) {
                // 복사가 완료된 경우 LoginActivity로 이동
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish() // 현재 액티비티 종료
            } else {
                // 복사하지 않은 경우 Snackbar 표시
                Snackbar.make(binding.lineTemporaryPassword, "임시 비밀번호를 복사해 주세요.", Snackbar.LENGTH_SHORT).apply {
                    anchorView = binding.btnTemporaryPasswordNext // 스낵바를 버튼 위에 고정
                }.show()
            }
        }
    }

    // 텍스트 복사 함수
    private fun copyToClipboard(text: String) {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Temporary Password", text)
        clipboard.setPrimaryClip(clip)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
