package com.example.hrr_android.access.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.example.hrr_android.R
import com.example.hrr_android.access.AuthViewModel
import com.example.hrr_android.access.ValidUtils
import com.example.hrr_android.databinding.FragmentVerificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerificationFragment : Fragment() {

    private var _binding: FragmentVerificationBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by activityViewModels() // 뷰 모델 초기화

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

        // 이메일 인증 API 호출 결과를 관찰하는 코드
        authViewModel.isVerified.observe(viewLifecycleOwner) { isVerified ->
            when (isVerified) {
                true -> {
                    ValidUtils.hideKeyboard(requireContext(), requireView())
                    ValidUtils.showSnackbar(requireView(), "인증 코드가 전송 되었습니다.", binding.lineVerification)
                    validateAndProceed() // 상태 업데이트
                }
                false -> {
                    ValidUtils.hideKeyboard(requireContext(), requireView())
                    ValidUtils.showSnackbar(requireView(), "인증 코드를 전송하지 못했습니다.", binding.lineVerification)
                }
                else -> {} // null이면 아무것도 안 함 (초기 상태)
            }
        }
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
        val email = binding.etVerificationEmail.text.toString()
        if (isEmailValid) {
            // 네트워크 요청 전에 기존 인증 상태 초기화
            authViewModel.setIsVerified(null)
            // 이메일 인증 코드를 서버로 요청 (InfoInputFragment와 동일한 API 호출)
            authViewModel.sendVerificationCode(email)

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
        }
    }

    private fun handleVerification() {
        val email = binding.etVerificationEmail.text.toString()
        val verificationCode = binding.etVerification.text.toString()

        ValidUtils.hideKeyboard(requireContext(), requireView())

        if (isEmailSent) {
            // 이메일 인증 코드 확인 요청
            authViewModel.confirmVerificationCode(email, verificationCode)

            // ViewModel의 응답에 따라 UI 업데이트
            authViewModel.verifiedUserId.observe(viewLifecycleOwner) { userId ->
                if (userId != null) {  // ID가 존재하면 인증 성공
                    isVerificationValid = true
                    binding.etVerification.isEnabled = false
                    binding.etVerification.setTextColor(ValidUtils.getTextColorDefault(requireContext()))

                    binding.btnVerification.isEnabled = false
                    binding.btnVerification.background = ValidUtils.getButtonInactiveBackground(requireContext())
                    binding.tvVerification.setTextColor(ValidUtils.getTextColorDefault(requireContext()))

                    ValidUtils.showSnackbar(requireView(), "이메일 인증이 완료되었습니다.", binding.lineVerification)
                    // 인증 완료 시 다음 버튼 활성화
                    updateNextButtonState()
                } else {
                    ValidUtils.hideKeyboard(requireContext(), requireView())
                    ValidUtils.showSnackbar(requireView(), "올바른 인증 코드를 입력해 주세요.", binding.lineVerification)
                }
            }
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
        // isVerificationValid가 true이면 다음 버튼 활성화
        ValidUtils.updateButtonState(
            binding.btnVerificationNext,
            binding.tvVerificationNext,
            binding.ivVerificationNext,
            isVerificationValid
        )
    }

    // 인증이 완료되었을 때 추가적인 처리가 필요하면 이 함수에서 처리 (여기서는 단순히 다음 버튼 상태 업데이트)
    private fun validateAndProceed() {
        updateNextButtonState()
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
