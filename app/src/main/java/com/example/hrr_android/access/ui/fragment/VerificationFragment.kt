package com.example.hrr_android.access.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.hrr_android.R
import com.example.hrr_android.access.ValidUtils
import com.example.hrr_android.databinding.FragmentVerificationBinding

class VerificationFragment : Fragment() {

    private var _binding: FragmentVerificationBinding? = null
    private val binding get() = _binding!!

    // 유효성 상태 변수 선언
    private var isEmailValid = false
    private var isEmailSent = false
    private var isVerificationValid = false

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
        setupVerificationProcess()
        setupNextButton()
        updateNextButtonState() // 초기 상태 설정
    }

    private fun initializeViews() {
        binding.etVerification.isEnabled = false
        binding.btnVerification.isEnabled = false
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
        // 이메일 유효성 검사 결과에 따라 UI 업데이트
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

    private fun setupVerificationProcess() {
        binding.btnVerificationSend.setOnClickListener { handleEmailSend() }
        binding.btnVerification.setOnClickListener { handleVerification() }
    }

    private fun handleEmailSend() {
        // 이메일 전송 처리
        if (isEmailValid) {
            isEmailSent = true
            binding.etVerificationEmail.isEnabled = false
            binding.etVerificationEmail.setTextColor(ValidUtils.getTextColorDefault(requireContext()))
            binding.btnVerificationSend.isEnabled = false
            binding.btnVerificationSend.background = ValidUtils.getButtonInactiveBackground(requireContext())
            binding.tvVerificationSend.setTextColor(ValidUtils.getTextColorDefault(requireContext()))
            binding.etVerification.isEnabled = true
            binding.btnVerification.isEnabled = true
            binding.btnVerification.background = ValidUtils.getButtonActiveBackground(requireContext())
            binding.tvVerification.setTextColor(ValidUtils.getTextColorError(requireContext()))
            ValidUtils.hideKeyboard(requireContext(), requireView())
            ValidUtils.showSnackbar(requireView(), "인증 코드가 전송 되었습니다.", binding.lineVerification)
        }
    }

    private fun handleVerification() {
        // 인증 코드 검증 처리
        val verificationCode = binding.etVerification.text.toString()
        ValidUtils.hideKeyboard(requireContext(), requireView())
        if (isEmailSent && verificationCode == "0202") { // 인증 코드 "0202"로 확인
            isVerificationValid = true
            binding.etVerification.isEnabled = false
            binding.etVerification.setTextColor(ValidUtils.getTextColorDefault(requireContext()))
            binding.btnVerification.isEnabled = false
            binding.btnVerification.background = ValidUtils.getButtonInactiveBackground(requireContext())
            binding.tvVerification.setTextColor(ValidUtils.getTextColorDefault(requireContext()))
            ValidUtils.showSnackbar(requireView(), "이메일 인증이 완료 되었습니다.", binding.lineVerification)
            updateNextButtonState() // 인증 완료 시 버튼 상태 업데이트
        } else {
            ValidUtils.showSnackbar(requireView(), "올바른 인증 코드를 입력해 주세요.", binding.lineVerification)
        }
    }

    private fun setupNextButton() {
        binding.btnVerificationNext.setOnClickListener {
            if (!isVerificationValid) {
                ValidUtils.showSnackbar(requireView(), "이메일 인증을 진행해 주세요.", binding.lineVerification)
            } else {
                // 이메일 인증 완료 시 다른 프래그먼트로 이동
                loadNextFragment(TemporaryPasswordFragment())
            }
        }
    }

    private fun updateNextButtonState() {
        val isEnabled = isVerificationValid

        ValidUtils.updateButtonState(
            binding.btnVerificationNext,
            binding.tvVerificationNext,
            binding.ivVerificationNext,
            isEnabled
        )
    }

    private fun loadNextFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.layout_password_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
