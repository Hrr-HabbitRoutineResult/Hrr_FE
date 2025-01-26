package com.example.hrr_android

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.hrr_android.databinding.FragmentInfoInputBinding
import com.google.android.material.snackbar.Snackbar

class InfoInputFragment : Fragment() {

    private var _binding: FragmentInfoInputBinding? = null
    private val binding get() = _binding!!

    // 유효성 상태 변수 선언
    private var isEmailValid = false
    private var isEmailSent = false
    private var isVerificationValid = false
    private var isPasswordValid = false
    private var isPasswordMatch = false

    private val defaultTextColor by lazy {
        ContextCompat.getColor(requireContext(), R.color.text_tertiary)
    }
    private val defaultBackground by lazy {
        ContextCompat.getDrawable(requireContext(), R.drawable.bg_input_field)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 뷰 바인딩 초기화
        _binding = FragmentInfoInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        setupNicknameValidation()
        setupEmailValidation()
        setupVerificationProcess()
        setupPasswordValidation()
        setupNextButton()
    }

    private fun initializeViews() {
        binding.etSignupVerification.isEnabled = false
        binding.btnSignupVerification.isEnabled = false
    }

    private fun setupNicknameValidation() {
        binding.etSignupNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val nickname = s.toString()
                if (isValidNickname(nickname)) {
                    updateNicknameUI(valid = true)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateNicknameUI(valid: Boolean) {
        if (valid) {
            binding.tvSignupNicknameHelper.text = "한글, 영어, 숫자의 조합으로 최대 10자까지 입력 가능합니다."
            binding.tvSignupNicknameHelper.setTextColor(defaultTextColor)
            binding.ivSignupNicknameError.visibility = View.GONE
            binding.etSignupNickname.background = defaultBackground
        } else {
            binding.tvSignupNicknameHelper.text = "사용할 수 없는 닉네임입니다."
            binding.tvSignupNicknameHelper.setTextColor(ContextCompat.getColor(requireContext(), R.color.sub_01))
            binding.ivSignupNicknameError.visibility = View.VISIBLE
            binding.etSignupNickname.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_input_field_error)
        }
    }

    private fun setupEmailValidation() {
        binding.etSignupEmail.addTextChangedListener {
            val email = it.toString()
            isEmailValid = isValidEmail(email)
            updateEmailUI()
        }
    }

    private fun updateEmailUI() {
        if (isEmailValid) {
            binding.btnSignupSend.isEnabled = true
            binding.btnSignupSend.background = ContextCompat.getDrawable(requireContext(), R.drawable.btn_orange_white_30)
            binding.tvSignupSend.setTextColor(ContextCompat.getColor(requireContext(), R.color.sub_01))
        } else {
            binding.btnSignupSend.isEnabled = false
            binding.btnSignupSend.background = ContextCompat.getDrawable(requireContext(), R.drawable.btn_grey_30)
            binding.tvSignupSend.setTextColor(defaultTextColor)
        }
    }

    private fun setupVerificationProcess() {
        binding.btnSignupSend.setOnClickListener { handleEmailSend() }
        binding.btnSignupVerification.setOnClickListener { handleVerification() }
    }

    private fun handleEmailSend() {
        if (isEmailValid) {
            isEmailSent = true
            binding.etSignupEmail.isEnabled = false
            binding.etSignupEmail.setTextColor(defaultTextColor)
            binding.btnSignupSend.isEnabled = false
            binding.btnSignupSend.background = ContextCompat.getDrawable(requireContext(), R.drawable.btn_grey_30)
            binding.tvSignupSend.setTextColor(defaultTextColor)
            binding.etSignupVerification.isEnabled = true
            binding.btnSignupVerification.isEnabled = true
            binding.btnSignupVerification.background = ContextCompat.getDrawable(requireContext(), R.drawable.btn_orange_white_30)
            binding.tvSignupVerification.setTextColor(ContextCompat.getColor(requireContext(), R.color.sub_01))
            hideKeyBoard()
            showSnackbar("인증 코드가 전송 되었습니다.")
        }
    }

    private fun handleVerification() {
        val verificationCode = binding.etSignupVerification.text.toString()
        hideKeyBoard()
        if (isEmailSent && verificationCode == "0202") {
            isVerificationValid = true
            binding.etSignupVerification.isEnabled = false
            binding.etSignupVerification.setTextColor(defaultTextColor)
            binding.btnSignupVerification.isEnabled = false
            binding.btnSignupVerification.background = ContextCompat.getDrawable(requireContext(), R.drawable.btn_grey_30)
            binding.tvSignupVerification.setTextColor(defaultTextColor)
            showSnackbar("이메일 인증이 완료 되었습니다.")
        } else {
            showSnackbar("올바른 인증 코드를 입력해 주세요.")
        }
    }

    private fun setupPasswordValidation() {
        binding.etSignupPassword.addTextChangedListener {
            val password = it.toString()
            isPasswordValid = isValidPassword(password)
            updatePasswordUI()
        }
    }

    private fun updatePasswordUI() {
        if (isPasswordValid) {
            binding.tvSignupPasswordHelper.text = "8자~20자 이하, 영대소문자, 숫자, 특수기호 2가지 이상 조합"
            binding.tvSignupPasswordHelper.setTextColor(defaultTextColor)
            binding.ivSignupPasswordError.visibility = View.GONE
            binding.etSignupPassword.background = defaultBackground
        } else {
            binding.tvSignupPasswordHelper.text = "사용 가능한 비밀번호 조합을 입력해 주세요"
            binding.tvSignupPasswordHelper.setTextColor(ContextCompat.getColor(requireContext(), R.color.sub_01))
            binding.ivSignupPasswordError.visibility = View.VISIBLE
            binding.etSignupPassword.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_input_field_error)
        }
    }

    private fun setupNextButton() {
        binding.btnInfoInputNext.setOnClickListener { validateAndProceed() }
    }

    private fun validateAndProceed() {
        val nickname = binding.etSignupNickname.text.toString()
        val password = binding.etSignupPassword.text.toString()
        val confirmPassword = binding.etSignupPasswordConfirm.text.toString()
        isPasswordMatch = password == confirmPassword

        when {
            isValidNickname(nickname) && isPasswordValid && isPasswordMatch && isVerificationValid -> {
                (activity as? SignUpActivity)?.changeFragment(CompleteFragment())
            }
            !isValidNickname(nickname) -> updateNicknameUI(valid = false)
            !isVerificationValid -> showSnackbar("이메일 인증을 진행해 주세요.")
            !isPasswordMatch -> {
                binding.tvSignupConfirmHelper.visibility = View.VISIBLE
                binding.ivSignupConfirmError.visibility = View.VISIBLE
                binding.etSignupPasswordConfirm.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_input_field_error)
            }
            else -> showSnackbar("입력 정보를 다시 확인해 주세요.")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 유효성 검사 함수
    private fun isValidNickname(nickname: String) = Regex("^[가-힣a-zA-Z0-9]{1,10}$").matches(nickname)
    private fun isValidEmail(email: String) = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    private fun isValidPassword(password: String) =
        Regex("^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,20}\$").containsMatchIn(password)

    // 키보드 숨기기
    private fun hideKeyBoard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = view?.rootView ?: requireActivity().window.decorView
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    // 스낵바 공통 함수
    private fun showSnackbar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).apply {
            anchorView = binding.lineInfoInput
        }.show()
    }
}
