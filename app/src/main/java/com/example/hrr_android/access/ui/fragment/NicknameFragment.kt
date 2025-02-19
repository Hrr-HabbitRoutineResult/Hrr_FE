package com.example.hrr_android.access.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.hrr_android.ProfileUpdateRequest
import com.example.hrr_android.UserViewModel
import com.example.hrr_android.access.AuthViewModel
import com.example.hrr_android.access.ValidUtils
import com.example.hrr_android.access.ui.SignUpActivity
import com.example.hrr_android.databinding.FragmentNicknameBinding
import kotlinx.coroutines.launch

class NicknameFragment : Fragment() {

    private var _binding: FragmentNicknameBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by activityViewModels() // 뷰 모델 초기화
    private val userViewModel: UserViewModel by activityViewModels()

    // 유효성 상태 변수 선언
    private var isNicknameValid = false // 닉네임 유효성 상태
    private var isNicknameChecked = false // 닉네임 중복 확인 여부

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNicknameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 초기화
        initializeViews()

        // 설정
        setupNicknameValidation()

        // 다음 버튼 클릭 시 CompleteFragment로 이동
        binding.btnNicknameNext.setOnClickListener {
            val profileToUpdate = ProfileUpdateRequest(
                name = binding.etNickname.text.toString(),
                profilePhoto = "",
                badges = listOf(null, null, null)
            )

            // suspend 함수 호출은 코루틴 내에서 처리
            lifecycleScope.launch {
                try {
                    userViewModel.updateProfile(profileToUpdate)
                    // API 호출 성공 시 플래그 저장
                    val prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    prefs.edit().putBoolean("isProfileUpdated", true).apply()

                    (activity as? SignUpActivity)?.changeFragment(CompleteFragment())
                } catch (e: Exception) {
                    // API 호출 실패 시 에러 처리
                    ValidUtils.showSnackbar(
                        requireView(),
                        "닉네임 설정에 실패하였습니다.",
                        binding.lineInfoInput
                    )
                }
            }
        }
    }

    private fun initializeViews() {
        // 초기 상태 설정
        validateAndProceed() // 초기 상태에서 버튼 상태 업데이트
    }

    private fun setupNicknameValidation() {
        binding.etNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val nickname = s.toString()
                isNicknameValid = ValidUtils.isValidNickname(nickname)

                updateNicknameUI(isNicknameValid, hasFocus = true)
                updateNicknameCheckButtonState()
                isNicknameChecked = false // 닉네임 변경 시 중복 확인 초기화
                validateAndProceed()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etNickname.setOnFocusChangeListener { _, hasFocus ->
            updateNicknameUI(isNicknameValid, hasFocus)
        }

        binding.btnNicknameCheck.setOnClickListener {
            val nickname = binding.etNickname.text.toString()
            authViewModel.checkNickname(nickname)
        }

        authViewModel.isNicknameAvailable.observe(viewLifecycleOwner) { isAvailable ->
            if (isAvailable) {
                isNicknameChecked = true
                ValidUtils.showSnackbar(requireView(), "사용 가능한 닉네임입니다.", binding.lineInfoInput)
                binding.tvNicknameHelper.setTextColor(ValidUtils.getTextColorDefault(requireContext()))
                // 닉네임 입력 비활성화
                binding.etNickname.isEnabled = false
                binding.etNickname.setTextColor(ValidUtils.getTextColorDefault(requireContext()))
                binding.btnNicknameCheck.isEnabled = false
                binding.btnNicknameCheck.background = ValidUtils.getButtonInactiveBackground(requireContext())
                binding.tvNicknameCheck.setTextColor(ValidUtils.getTextColorDefault(requireContext()))

            } else {
                isNicknameChecked = false
                binding.tvNicknameHelper.text = "이미 사용 중인 닉네임입니다."
                binding.tvNicknameHelper.setTextColor(ValidUtils.getTextColorError(requireContext()))
            }
            validateAndProceed()
        }
    }

    private fun updateNicknameCheckButtonState() {
        if (isNicknameValid) {
            binding.btnNicknameCheck.isEnabled = true
            binding.btnNicknameCheck.background = ValidUtils.getButtonActiveBackground(requireContext())
            binding.tvNicknameCheck.setTextColor(ValidUtils.getTextColorError(requireContext()))
        } else {
            binding.btnNicknameCheck.isEnabled = false
            binding.btnNicknameCheck.background = ValidUtils.getButtonInactiveBackground(requireContext())
            binding.tvNicknameCheck.setTextColor(ValidUtils.getTextColorDefault(requireContext()))
        }
    }

    private fun updateNicknameUI(valid: Boolean, hasFocus: Boolean) {
        // 닉네임 유효성 검사 결과와 포커스 여부에 따라 UI 업데이트
        if (valid || !hasFocus) {
            binding.tvNicknameHelper.text = "한글, 영어, 숫자의 조합으로 최대 10자까지 입력 가능합니다."
            binding.tvNicknameHelper.setTextColor(ValidUtils.getTextColorDefault(requireContext()))
            binding.ivNicknameError.visibility = View.GONE
            binding.etNickname.background = ValidUtils.getBackgroundDefault(requireContext())
        } else {
            binding.tvNicknameHelper.text = "사용할 수 없는 닉네임입니다."
            binding.tvNicknameHelper.setTextColor(ValidUtils.getTextColorError(requireContext()))
            binding.ivNicknameError.visibility = View.VISIBLE
            binding.etNickname.background = ValidUtils.getBackgroundError(requireContext())
        }
    }

    private fun validateAndProceed() {
        val nickname = binding.etNickname.text.toString()
        val isNicknameValid = ValidUtils.isValidNickname(nickname)

        // 입력값 상태를 기반으로 버튼 활성화/비활성화
        val isButtonEnabled = isNicknameValid && isNicknameChecked

        updateNextButtonState(isButtonEnabled)
    }

    // 버튼 활성화/비활성화 상태를 업데이트하는 함수
    private fun updateNextButtonState(isEnabled: Boolean) {
        ValidUtils.updateButtonState(
            binding.btnNicknameNext,
            binding.tvNicknameNext,
            binding.ivNicknameNext,
            isEnabled
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}