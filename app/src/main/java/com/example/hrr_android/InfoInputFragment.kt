package com.example.hrr_android

import android.content.Context
import android.os.Bundle
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

        val defaultTextColor = ContextCompat.getColor(requireContext(), R.color.text_tertiary)
        val defaultBackground = ContextCompat.getDrawable(requireContext(), R.drawable.bg_input_field)

        // 초기 상태 설정
        binding.etSignupVerification.isEnabled = false
        binding.btnSignupVerification.isEnabled = false

        // 이메일 유효성 검사
        binding.etSignupEmail.addTextChangedListener {
            val email = it.toString()
            isEmailValid = if (isValidEmail(email)) {
                binding.btnSignupSend.isEnabled = true
                binding.btnSignupSend.background = ContextCompat.getDrawable(requireContext(), R.drawable.btn_orange_white_30)
                binding.tvSignupSend.setTextColor(ContextCompat.getColor(requireContext(), R.color.sub_01))
                true
            } else {
                binding.btnSignupSend.isEnabled = false
                binding.btnSignupSend.background = ContextCompat.getDrawable(requireContext(), R.drawable.btn_grey_30)
                binding.tvSignupSend.setTextColor(defaultTextColor)
                false
            }
        }

        // 전송 버튼 클릭 시
        binding.btnSignupSend.setOnClickListener {
            if (isEmailValid) {
                // 이메일 전송 로직
                isEmailSent = true

                // 이메일 EditText 비활성화
                binding.etSignupEmail.isEnabled = false
                binding.etSignupEmail.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_tertiary))

                // 전송 버튼 비활성화
                binding.btnSignupSend.isEnabled = false
                binding.btnSignupSend.background = ContextCompat.getDrawable(requireContext(), R.drawable.btn_grey_30)
                binding.tvSignupSend.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_tertiary))

                // 인증번호 EditText 및 인증 버튼 활성화
                binding.etSignupVerification.isEnabled = true
                binding.btnSignupVerification.isEnabled = true
                binding.btnSignupVerification.background = ContextCompat.getDrawable(requireContext(), R.drawable.btn_orange_white_30)
                binding.tvSignupVerification.setTextColor(ContextCompat.getColor(requireContext(), R.color.sub_01))

                hideKeyBoard()
                showSnackbar("인증 코드가 전송 되었습니다.")
            }
        }

        // 인증 버튼 클릭 시
        binding.btnSignupVerification.setOnClickListener {
            val verificationCode = binding.etSignupVerification.text.toString()
            hideKeyBoard()
            if (isEmailSent && verificationCode == "0202") {  // 인증 코드 확인 로직
                isVerificationValid = true

                // 인증번호 EditText 비활성화
                binding.etSignupVerification.isEnabled = false
                binding.etSignupVerification.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_tertiary))

                // 인증 버튼 비활성화
                binding.btnSignupVerification.isEnabled = false
                binding.btnSignupVerification.background = ContextCompat.getDrawable(requireContext(), R.drawable.btn_grey_30)
                binding.tvSignupVerification.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_tertiary))

                showSnackbar("이메일 인증이 완료 되었습니다.")
            } else {
                showSnackbar("올바른 인증 코드를 입력해 주세요.")
            }
        }

        // 비밀번호 실시간 유효성 검사
        binding.etSignupPassword.addTextChangedListener {
            val password = it.toString()
            isPasswordValid = if (!isValidPassword(password)) {  // 비밀번호 형식에 부합하지 않을 때
                binding.tvSignupPasswordHelper.text = "사용 가능한 비밀번호 조합을 입력해 주세요"
                binding.tvSignupPasswordHelper.setTextColor(ContextCompat.getColor(requireContext(), R.color.sub_01))
                binding.ivSignupPasswordError.visibility = View.VISIBLE
                binding.etSignupPassword.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_input_field_error)
                false
            } else {  // 비밀번호 형식에 부합할 때
                binding.tvSignupPasswordHelper.text = "8자~20자 이하, 영대소문자, 숫자, 특수기호 2가지 이상 조합"
                binding.tvSignupPasswordHelper.setTextColor(defaultTextColor)
                binding.ivSignupPasswordError.visibility = View.GONE
                binding.etSignupPassword.background = defaultBackground
                true
            }
        }

        // EditText 포커스 변경 리스너 추가
        binding.etSignupPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {  // 포커스를 잃었을 때
                binding.tvSignupPasswordHelper.text = "8자~20자 이하, 영대소문자, 숫자, 특수기호 2가지 이상 조합"
                binding.tvSignupPasswordHelper.setTextColor(defaultTextColor)
                binding.ivSignupPasswordError.visibility = View.GONE
                binding.etSignupPassword.background = defaultBackground
            }
        }

        // 다음 버튼 클릭 시 유효성 검사 및 화면 전환
        binding.btnInfoInputNext.setOnClickListener {
            val password = binding.etSignupPassword.text.toString()
            val confirmPassword = binding.etSignupPasswordConfirm.text.toString()
            isPasswordMatch = password == confirmPassword

            if (isEmailValid && isPasswordValid && isPasswordMatch && isVerificationValid) {
                // 모든 조건 충족 시 다음 화면으로 이동
                (activity as? SignUpActivity)?.changeFragment(CompleteFragment())
            } else if (!isVerificationValid) {
                // 이메일 인증을 진행하지 않았을 때
                showSnackbar("이메일 인증을 진행해 주세요.")
            } else if (!isPasswordMatch) {
                binding.tvSignupConfirmHelper.visibility = View.VISIBLE
                binding.ivSignupConfirmError.visibility = View.VISIBLE
                binding.etSignupPasswordConfirm.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_input_field_error)
            }
            else {
                showSnackbar("입력 정보를 다시 확인해 주세요.")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 이메일 유효성 검사 함수
    private fun isValidEmail(email: String): Boolean {
        // 이메일 형식 체크
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // 비밀번호 유효성 검사 함수
    private fun isValidPassword(password: String): Boolean {
        // (8~20자, 영문/숫자/특수문자 중 2가지 포함)
        val passwordPattern = "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,20}\$"
        return Regex(passwordPattern).containsMatchIn(password)
    }

    // 키보드 숨기기
    private fun hideKeyBoard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = view?.rootView ?: requireActivity().window.decorView
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    // 스낵바 공통 함수
    private fun showSnackbar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).apply {
            anchorView = binding.lineInfoInput  // 특정 버튼 위에 고정
        }.show()
    }
}