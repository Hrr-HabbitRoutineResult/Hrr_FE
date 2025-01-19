package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.hrr_android.databinding.FragmentInfoInputBinding

class InfoInputFragment : Fragment() {

    private var _binding: FragmentInfoInputBinding? = null
    private val binding get() = _binding!!

    // 유효성 상태 변수 선언
    private var isEmailValid = false
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

        // 이메일 실시간 유효성 검사
        binding.etSignupEmail.addTextChangedListener {
            val email = it.toString()
            isEmailValid = if (!isValidEmail(email)) {  // 이메일 형식에 부합하지 않을 때
                binding.tvSignupEmailHelper.text = "올바른 이메일 주소를 입력해 주세요."
                binding.tvSignupEmailHelper.setTextColor(ContextCompat.getColor(requireContext(), R.color.sub_01))
                binding.ivSignupEmailError.visibility = View.VISIBLE
                binding.etSignupEmail.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_input_field_error)
                false
            } else {  // 이메일 형식에 부합할 때
                binding.tvSignupEmailHelper.text = ""
                binding.tvSignupEmailHelper.setTextColor(defaultTextColor)
                binding.ivSignupEmailError.visibility = View.GONE
                binding.etSignupEmail.background = defaultBackground
                true
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
                binding.tvSignupPasswordHelper.text = ""
                binding.tvSignupPasswordHelper.setTextColor(defaultTextColor)
                binding.ivSignupPasswordError.visibility = View.GONE
                binding.etSignupPassword.background = defaultBackground
                true
            }
        }

        // 다음 버튼 클릭 시 유효성 검사 및 화면 전환
        binding.btnInfoInputNext.setOnClickListener {
            val password = binding.etSignupPassword.text.toString()
            val confirmPassword = binding.etSignupPasswordConfirm.text.toString()
            isPasswordMatch = password == confirmPassword

            if (isPasswordMatch && isEmailValid && isPasswordValid) {
                // 모든 조건 충족 시 다음 화면으로 이동
                (activity as? SignUpActivity)?.changeFragment(CompleteFragment())
            } else {
                // 조건 미충족 시 에러 메시지
                if (!isPasswordMatch) {
                    binding.tvSignupConfirmHelper.visibility = View.VISIBLE
                    binding.ivSignupConfirmError.visibility = View.VISIBLE
                    binding.etSignupPasswordConfirm.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_input_field_error)
                } else {
                    binding.tvSignupConfirmHelper.visibility = View.GONE
                    binding.ivSignupConfirmError.visibility = View.GONE
                    binding.etSignupPasswordConfirm.background = defaultBackground
                    Toast.makeText(requireContext(), "입력 정보를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
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
}