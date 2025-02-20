package com.example.hrr_android.access.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hrr_android.R
import com.example.hrr_android.access.AuthViewModel
import com.example.hrr_android.access.ValidUtils
import com.example.hrr_android.databinding.FragmentPasswordResetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordResetFragment : Fragment() {

    private var _binding: FragmentPasswordResetBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    // 유효성 상태 변수
    private var isPasswordNowValid = false
    private var isPasswordNowMatch = false
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

        initializeViews()

        setupPasswordNowValidation()
        setupPasswordValidation()
        setupPasswordMatchValidation()
        setupNextButton()
        updateNextButtonState() // 초기 상태 설정
        // setupEnterKeyListener()

        observeViewModel()
    }

    private fun initializeViews() {
        // 초기 상태 설정
        binding.layoutResetPasswordContainer.visibility = View.GONE
    }

    private fun setupPasswordNowValidation() {
        binding.etResetPasswordNow.addTextChangedListener {
            val passwordNow = it.toString()
            isPasswordNowValid = passwordNow.isNotEmpty()
            updateNextButtonState() // 상태 갱신
        }
    }

    private fun observeViewModel() {
        authViewModel.passwordCheckResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                isPasswordNowMatch = true
                updatePasswordNowUI()
                binding.layoutResetPasswordContainer.visibility = View.VISIBLE
                isPasswordNowValid = false // 다음 단계로 진행되므로 초기화
            }.onFailure {
                isPasswordNowMatch = false
                ValidUtils.hideKeyboard(requireContext(), requireView())
                ValidUtils.showSnackbar(requireView(), "현재 비밀번호가 일치하지 않습니다.", binding.lineResetSecond)
            }
            updateNextButtonState() // 상태 갱신
        }

        authViewModel.passwordNewResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                loadNextFragment(PasswordResetCompleteFragment()) // 성공 시 다음 화면으로 이동
            }.onFailure {
                ValidUtils.showSnackbar(requireView(), "비밀번호 변경에 실패했습니다.", binding.lineResetSecond)
            }
        }
    }

    private fun updatePasswordNowUI() {
        if (isPasswordNowValid || isPasswordNowMatch) {
            binding.etResetPasswordNow.isEnabled = false
            binding.etResetPasswordNow.setTextColor(ValidUtils.getTextColorDefault(requireContext()))
            isPasswordNowValid = false
        }
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
            binding.tvResetPasswordHelper.setTextColor(ValidUtils.getTextColorDefault(requireContext()))
            binding.ivResetPasswordError.visibility = View.GONE
            binding.etResetPassword.background = ValidUtils.getBackgroundDefault(requireContext())
        } else {
            binding.tvResetPasswordHelper.text = "사용 가능한 비밀번호 조합을 입력해 주세요"
            binding.tvResetPasswordHelper.setTextColor(ValidUtils.getTextColorError(requireContext()))
            binding.ivResetPasswordError.visibility = View.VISIBLE
            binding.etResetPassword.background = ValidUtils.getBackgroundError(requireContext())
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
            binding.etResetPasswordConfirm.background = ValidUtils.getBackgroundDefault(requireContext())
        } else {
            binding.tvResetPasswordConfirmHelper.visibility = View.VISIBLE
            binding.ivResetPasswordConfirmError.visibility = View.VISIBLE
            binding.etResetPasswordConfirm.background = ValidUtils.getBackgroundError(requireContext())
        }
    }

    private fun updateNextButtonState() {
        val isEnabled = isPasswordNowValid || (isPasswordNowMatch && isPasswordValid && isPasswordMatch)

        ValidUtils.updateButtonState(
            binding.btnResetPasswordNext,
            binding.tvResetPasswordNext,
            binding.ivResetPasswordNext,
            isEnabled
        )
    }
    private fun setupNextButton() {
        binding.btnResetPasswordNext.setOnClickListener {
            val currentPassword = binding.etResetPasswordNow.text.toString()
            val newPassword = binding.etResetPassword.text.toString()

            // 현재 비밀번호 검증 단계 (ViewModel 호출 추가)
            if (isPasswordNowValid) {
                authViewModel.passwordCheck(currentPassword) // 비밀번호 확인 API 호출
                return@setOnClickListener
            }

            // 새 비밀번호가 현재 비밀번호와 동일한 경우
            if (currentPassword == newPassword) {
                ValidUtils.hideKeyboard(requireContext(), requireView())
                ValidUtils.showSnackbar(requireView(), "새 비밀번호는 현재 비밀번호와 다르게 설정해주세요.", binding.lineResetSecond)
                return@setOnClickListener
            }

            // 새 비밀번호 설정 단계 (API 호출 추가)
            if (isPasswordValid && isPasswordMatch) {
                authViewModel.passwordNew(newPassword) // 새 비밀번호 변경 API 호출
            }
        }
    }

    private fun loadNextFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.layout_password_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }
/*
    private fun setupEnterKeyListener() {
        // 현재 비밀번호 입력란에서 엔터 키를 눌렀을 때
        ValidUtils.setEnterKeyListener(binding.etResetPasswordNow, binding.btnResetPasswordNext)

        // 새 비밀번호 입력란에서 엔터 키를 눌렀을 때
        ValidUtils.setEnterKeyListener(binding.etResetPassword, binding.btnResetPasswordNext)

        // 새 비밀번호 확인 입력란에서 엔터 키를 눌렀을 때
        ValidUtils.setEnterKeyListener(binding.etResetPasswordConfirm, binding.btnResetPasswordNext)
    }
*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
