package com.example.hrr_android.access.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.example.hrr_android.R
import com.example.hrr_android.access.AuthViewModel
import com.example.hrr_android.access.ValidUtils
import com.example.hrr_android.access.ui.LoginActivity
import com.example.hrr_android.databinding.FragmentVerificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerificationFragment : Fragment() {

    private var _binding: FragmentVerificationBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by activityViewModels() // 뷰 모델 초기화

    // 유효성 상태 변수 선언
    private var isEmailValid = false
    private var isPasswordResetValid = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 뷰 바인딩 초기화
        _binding = FragmentVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        setupEmailValidation()
        setupPasswordResetProcess()
        setupNextButton()
        updateNextButtonState() // 초기 상태 설정

        // 비밀번호 재설정 API 호출 결과를 관찰하는 코드
        authViewModel.passwordResetResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                ValidUtils.hideKeyboard(requireContext(), requireView())
                ValidUtils.showSnackbar(requireView(), "임시 비밀번호가 전송되었습니다.", binding.lineVerification)

                // 비밀번호 재설정 성공 시 다음 버튼 활성화
                isPasswordResetValid = true
                updateNextButtonState()
            }.onFailure {
                ValidUtils.hideKeyboard(requireContext(), requireView())
                ValidUtils.showSnackbar(requireView(), "존재하지 않는 이메일입니다.", binding.lineVerification)
                binding.etVerificationEmail.isEnabled = true
                binding.etVerificationEmail.setTextColor(ValidUtils.getTextColorActive(requireContext()))
            }
        }

    }

    private fun initializeViews() {
        binding.btnVerificationNext.isEnabled = false // 초기 상태 비활성화
    }

    private fun setupEmailValidation() {
        binding.etVerificationEmail.addTextChangedListener {
            val email = it.toString()
            isEmailValid = ValidUtils.isValidEmail(email)
            updateEmailUI()
        }
    }

    private fun updateEmailUI() {
        if (isEmailValid) {
            binding.btnVerificationSend.isEnabled = true
            binding.btnVerificationSend.background = ValidUtils.getButtonActiveBackground(requireContext())
            binding.tvVerificationSend.setTextColor(ValidUtils.getTextColorError(requireContext()))
        } else {
            binding.btnVerificationSend.isEnabled = false
            binding.btnVerificationSend.background = ValidUtils.getButtonInactiveBackground(requireContext())
            binding.tvVerificationSend.setTextColor(ValidUtils.getTextColorDefault(requireContext()))
        }
    }

    private fun setupPasswordResetProcess() {
        binding.btnVerificationSend.setOnClickListener { handlePasswordReset() }
    }

    private fun handlePasswordReset() {
        val email = binding.etVerificationEmail.text.toString()
        if (isEmailValid) {
            authViewModel.passwordReset(email)

            binding.etVerificationEmail.isEnabled = false
            binding.etVerificationEmail.setTextColor(ValidUtils.getTextColorDefault(requireContext()))
            binding.btnVerificationSend.isEnabled = false
            binding.btnVerificationSend.background = ValidUtils.getButtonInactiveBackground(requireContext())
            binding.tvVerificationSend.setTextColor(ValidUtils.getTextColorDefault(requireContext()))
            ValidUtils.hideKeyboard(requireContext(), requireView())
        }
    }

    private fun setupNextButton() {
        binding.btnVerificationNext.setOnClickListener {
            navigateToLoginActivity()
        }
    }

    private fun updateNextButtonState() {
        ValidUtils.updateButtonState(
            binding.btnVerificationNext,
            binding.tvVerificationNext,
            binding.ivVerificationNext,
            isPasswordResetValid
        )
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        requireActivity().finish() // 현재 액티비티 종료
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
