package com.example.hrr_android.access.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.hrr_android.R
import com.example.hrr_android.access.ValidUtils
import com.example.hrr_android.databinding.FragmentPasswordResetBinding

class PasswordResetFragment : Fragment() {

    private var _binding: FragmentPasswordResetBinding? = null
    private val binding get() = _binding!!

    // 유효성 상태 변수
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
        _binding = FragmentPasswordResetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPasswordValidation()
        setupPasswordMatchValidation()
        setupNextButton()
    }

    private fun setupPasswordValidation() {
        // 비밀번호 입력 시 유효성 검사 수행
        binding.etResetPassword.addTextChangedListener {
            val password = it.toString()
            isPasswordValid = ValidUtils.isValidPassword(password)
            updatePasswordUI(hasFocus = true) // 입력 중일 때는 항상 유효성만 갱신
        }

        // 포커스 이동 시 UI 업데이트
        binding.etResetPassword.setOnFocusChangeListener { _, hasFocus ->
            updatePasswordUI(hasFocus = hasFocus)
        }
    }

    private fun updatePasswordUI(hasFocus: Boolean) {
        // 비밀번호 유효성 검사 결과에 따라 UI 업데이트
        if (isPasswordValid || !hasFocus) {
            binding.tvResetPasswordHelper.text = "8자~20자 이하, 영대소문자, 숫자, 특수기호 2가지 이상 조합"
            binding.tvResetPasswordHelper.setTextColor(defaultTextColor)
            binding.ivResetPasswordError.visibility = View.GONE
            binding.etResetPassword.background = defaultBackground
        } else {
            binding.tvResetPasswordHelper.text = "사용 가능한 비밀번호 조합을 입력해 주세요"
            binding.tvResetPasswordHelper.setTextColor(ContextCompat.getColor(requireContext(),
                R.color.sub_01
            ))
            binding.ivResetPasswordError.visibility = View.VISIBLE
            binding.etResetPassword.background = ContextCompat.getDrawable(requireContext(),
                R.drawable.bg_input_field_error
            )
        }
    }

    private fun setupPasswordMatchValidation() {
        // 비밀번호와 확인 입력란의 일치 여부 확인
        val passwordWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = binding.etResetPassword.text.toString()
                val confirmPassword = binding.etResetPasswordConfirm.text.toString()
                isPasswordMatch = password == confirmPassword
                if (binding.etResetPasswordConfirm.hasFocus()) { // 비밀번호 확인 필드가 포커스를 가질 때만 UI 업데이트
                    updatePasswordMatchUI()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.etResetPassword.addTextChangedListener(passwordWatcher)
        binding.etResetPasswordConfirm.addTextChangedListener(passwordWatcher)

        // 비밀번호 확인 필드 포커스 상태 변경 리스너 설정
        binding.etResetPasswordConfirm.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // 포커스를 가졌을 때만 일치 여부를 확인하고 UI 업데이트
                val password = binding.etResetPassword.text.toString()
                val confirmPassword = binding.etResetPasswordConfirm.text.toString()
                isPasswordMatch = password == confirmPassword
                updatePasswordMatchUI()
            } else {
                // 포커스를 잃으면 UI를 기본 상태로 되돌리기
                binding.tvResetPasswordConfirmHelper.visibility = View.GONE
                binding.ivResetPasswordConfirmError.visibility = View.GONE
                binding.etResetPasswordConfirm.background = defaultBackground
            }
        }
    }

    private fun updatePasswordMatchUI() {
        // 비밀번호 일치 여부에 따라 UI 업데이트
        if (isPasswordMatch) {
            binding.tvResetPasswordConfirmHelper.visibility = View.GONE
            binding.ivResetPasswordConfirmError.visibility = View.GONE
            binding.etResetPasswordConfirm.background = defaultBackground
        } else {
            binding.tvResetPasswordConfirmHelper.visibility = View.VISIBLE
            binding.ivResetPasswordConfirmError.visibility = View.VISIBLE
            binding.etResetPasswordConfirm.background = ContextCompat.getDrawable(requireContext(),
                R.drawable.bg_input_field_error
            )
        }
    }

    private fun setupNextButton() {
        binding.btnResetPasswordNext.setOnClickListener { validateAndProceed() }
    }

    private fun validateAndProceed() {
        val password = binding.etResetPassword.text.toString()
        val confirmPassword = binding.etResetPasswordConfirm.text.toString()

        // 비밀번호 입력 여부 확인
        if (password.isEmpty()) {
            ValidUtils.showSnackbar(requireView(), "비밀번호를 입력해 주세요.", binding.lineResetSecond)
            binding.etResetPassword.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_input_field_error)
            return
        }

        // 비밀번호 확인 입력 여부 확인
        if (confirmPassword.isEmpty()) {
            ValidUtils.showSnackbar(requireView(), "비밀번호 확인란을 입력해 주세요.", binding.lineResetSecond)
            binding.etResetPasswordConfirm.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_input_field_error)
            return
        }

        if (isPasswordValid && isPasswordMatch) {
            loadNextFragment(PasswordResetCompleteFragment())
        } else if (!isPasswordValid) {
            ValidUtils.showSnackbar(requireView(), "비밀번호가 일치하지 않습니다.", binding.lineResetSecond)
        }
            binding.tvResetPasswordConfirmHelper.visibility = View.VISIBLE
            binding.ivResetPasswordConfirmError.visibility = View.VISIBLE
            binding.etResetPasswordConfirm.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_input_field_error)
    }

    // 프래그먼트 교체 함수
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
