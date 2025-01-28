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
        updateNextButtonState() // 초기 상태 설정
    }

    private fun setupPasswordValidation() {
        binding.etResetPassword.addTextChangedListener {
            val password = it.toString()
            isPasswordValid = ValidUtils.isValidPassword(password)
            updatePasswordUI(hasFocus = true)
            updateNextButtonState() // 상태 갱신
        }

        binding.etResetPassword.setOnFocusChangeListener { _, hasFocus ->
            updatePasswordUI(hasFocus)
            updateNextButtonState() // 상태 갱신
        }
    }

    private fun updatePasswordUI(hasFocus: Boolean) {
        if (isPasswordValid || !hasFocus) {
            binding.tvResetPasswordHelper.text = "8자~20자 이하, 영대소문자, 숫자, 특수기호 2가지 이상 조합"
            binding.tvResetPasswordHelper.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_tertiary))
            binding.ivResetPasswordError.visibility = View.GONE
            binding.etResetPassword.background = ContextCompat.getDrawable(requireContext(),
                R.drawable.bg_input_field
            )
        } else {
            binding.tvResetPasswordHelper.text = "사용 가능한 비밀번호 조합을 입력해 주세요"
            binding.tvResetPasswordHelper.setTextColor(ContextCompat.getColor(requireContext(), R.color.sub_01))
            binding.ivResetPasswordError.visibility = View.VISIBLE
            binding.etResetPassword.background = ContextCompat.getDrawable(requireContext(),
                R.drawable.bg_input_field_error
            )
        }
    }

    private fun setupPasswordMatchValidation() {
        val passwordWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = binding.etResetPassword.text.toString()
                val confirmPassword = binding.etResetPasswordConfirm.text.toString()
                isPasswordMatch = password == confirmPassword
                if (binding.etResetPasswordConfirm.hasFocus()) {
                    updatePasswordMatchUI()
                }
                updateNextButtonState() // 상태 갱신
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.etResetPassword.addTextChangedListener(passwordWatcher)
        binding.etResetPasswordConfirm.addTextChangedListener(passwordWatcher)
    }

    private fun updatePasswordMatchUI() {
        if (isPasswordMatch) {
            binding.tvResetPasswordConfirmHelper.visibility = View.GONE
            binding.ivResetPasswordConfirmError.visibility = View.GONE
            binding.etResetPasswordConfirm.background = ContextCompat.getDrawable(requireContext(),
                R.drawable.bg_input_field
            )
        } else {
            binding.tvResetPasswordConfirmHelper.visibility = View.VISIBLE
            binding.ivResetPasswordConfirmError.visibility = View.VISIBLE
            binding.etResetPasswordConfirm.background = ContextCompat.getDrawable(requireContext(),
                R.drawable.bg_input_field_error
            )
        }
    }

    private fun updateNextButtonState() {
        val isEnabled = isPasswordValid && isPasswordMatch

        ValidUtils.updateButtonState(
            binding.btnResetPasswordNext,
            binding.tvResetPasswordNext,
            binding.ivResetPasswordNext,
            isEnabled
        )
    }


    private fun setupNextButton() {
        binding.btnResetPasswordNext.setOnClickListener {
            if (isPasswordValid && isPasswordMatch) {
                loadNextFragment(PasswordResetCompleteFragment())
            }
        }
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
