package com.example.hrr_android.access.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.hrr_android.access.ValidUtils
import com.example.hrr_android.databinding.FragmentInfoInputBinding
import com.example.hrr_android.access.AuthViewModel
import com.example.hrr_android.access.model.RegisterRequest
import com.example.hrr_android.access.model.RegisterResponse
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.hrr_android.access.ui.LoginActivity
import com.example.hrr_android.access.ui.SignUpActivity

class InfoInputFragment : Fragment() {

    private var _binding: FragmentInfoInputBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by activityViewModels() // 뷰 모델 초기화

    // 유효성 상태 변수 선언
    private var isEmailValid = false // 이메일 유효성 상태
    private var isEmailSent = false // 이메일 전송 여부
    private var isVerificationValid = false // 인증 코드 유효성 상태
    private var isPasswordValid = false // 비밀번호 유효성 상태
    private var isPasswordMatch = false // 비밀번호 일치 여부

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
        // 초기화
        initializeViews()

        // 설정
        setupNicknameValidation()
        setupEmailValidation()
        setupVerificationProcess()
        setupPasswordValidation()
        setupPasswordMatchValidation()

        // 회원가입 API 결과 관찰
        observeRegistrationResult()

        // 버튼 클릭 리스너
        binding.btnInfoInputNext.setOnClickListener {
            if (binding.btnInfoInputNext.isEnabled) {
                // 모든 입력값 유효성 검사 후 회원가입 API 호출
                val email = binding.etSignupEmail.text.toString()
                val password = binding.etSignupPassword.text.toString()
                val nickname = binding.etSignupNickname.text.toString()

                val verificationId = authViewModel.verifiedUserId.value ?: 0

                val registerRequest = RegisterRequest(email, password, nickname, verificationId)
                authViewModel.registerUser(registerRequest)
            }
        }
    }

    private fun observeRegistrationResult() {
        authViewModel.registrationResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { registerResponse ->
                saveUserInfo(registerResponse)
                (activity as? SignUpActivity)?.let { activity ->
                    activity.startActivity(Intent(activity, LoginActivity::class.java))
                    activity.finish()
                }
            }.onFailure {
                ValidUtils.hideKeyboard(requireContext(), requireView())
                ValidUtils.showSnackbar(requireView(), "회원가입에 실패하였습니다.", binding.lineInfoInput)
            }
        }
    }

    private fun saveUserInfo(registerResponse: RegisterResponse) {
        val user = registerResponse.createdUser
        // MasterKey 생성
        val masterKey = MasterKey.Builder(requireContext())
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        // EncryptedSharedPreferences 인스턴스 생성
        val sharedPreferences = EncryptedSharedPreferences.create(
            requireContext(),
            "user_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        // 사용자 정보 저장
        sharedPreferences.edit().apply {
            putInt("user_id", user.id)
            putString("user_email", user.email)
            putString("user_nickname", user.nickname)
            apply()
        }

        // 저장된 값을 읽어와 로그로 출력하여 저장 여부 확인
        val savedUserId = sharedPreferences.getInt("user_id", -1)
        val savedEmail = sharedPreferences.getString("user_email", "none")
        val savedNickname = sharedPreferences.getString("user_nickname", "none")
        Log.d("UserInfo", "Saved user info: id=$savedUserId, email=$savedEmail, nickname=$savedNickname")
    }

    private fun initializeViews() {
        // 초기 상태 설정
        binding.etSignupVerification.isEnabled = false
        binding.btnSignupVerification.isEnabled = false
        validateAndProceed() // 초기 상태에서 버튼 상태 업데이트
    }

    private fun setupNicknameValidation() {
        // 닉네임 입력 시 유효성 검사 수행
        binding.etSignupNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val nickname = s.toString()
                val isValid = ValidUtils.isValidNickname(nickname)
                updateNicknameUI(isValid, hasFocus = true) // 닉네임 입력 중 유효성 업데이트
                validateAndProceed() // 전체 버튼 상태 업데이트
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 포커스 상태 변경 시 기본 상태로 복원
        binding.etSignupNickname.setOnFocusChangeListener { _, hasFocus ->
            val nickname = binding.etSignupNickname.text.toString()
            val isValid = ValidUtils.isValidNickname(nickname)
            updateNicknameUI(isValid, hasFocus) // 포커스 변경 시 상태 업데이트
        }
    }


    private fun updateNicknameUI(valid: Boolean, hasFocus: Boolean) {
        // 닉네임 유효성 검사 결과와 포커스 여부에 따라 UI 업데이트
        if (valid || !hasFocus) {
            binding.tvSignupNicknameHelper.text = "한글, 영어, 숫자의 조합으로 최대 10자까지 입력 가능합니다."
            binding.tvSignupNicknameHelper.setTextColor(ValidUtils.getTextColorDefault(requireContext()))
            binding.ivSignupNicknameError.visibility = View.GONE
            binding.etSignupNickname.background = ValidUtils.getBackgroundDefault(requireContext())
        } else {
            binding.tvSignupNicknameHelper.text = "사용할 수 없는 닉네임입니다."
            binding.tvSignupNicknameHelper.setTextColor(ValidUtils.getTextColorError(requireContext()))
            binding.ivSignupNicknameError.visibility = View.VISIBLE
            binding.etSignupNickname.background = ValidUtils.getBackgroundError(requireContext())
        }
    }


    private fun setupEmailValidation() {
        // 이메일 입력 시 유효성 검사 수행
        binding.etSignupEmail.addTextChangedListener {
            val email = it.toString()
            isEmailValid = ValidUtils.isValidEmail(email)
            updateEmailUI()
            validateAndProceed() // 상태 업데이트
        }
    }

    private fun updateEmailUI() {
        // 이메일 유효성 검사 결과에 따라 UI 업데이트
        if (isEmailValid) {
            binding.btnSignupSend.isEnabled = true
            binding.btnSignupSend.background = ValidUtils.getButtonActiveBackground(requireContext())
            binding.tvSignupSend.setTextColor(ValidUtils.getTextColorError(requireContext()))
        } else {
            binding.btnSignupSend.isEnabled = false
            binding.btnSignupSend.background = ValidUtils.getButtonInactiveBackground(requireContext())
            binding.tvSignupSend.setTextColor(ValidUtils.getTextColorDefault(requireContext()))
        }
    }

    private fun setupVerificationProcess() {
        // 이메일 전송 및 인증 처리 설정
        binding.btnSignupSend.setOnClickListener {
            handleEmailSend()
        }
        binding.btnSignupVerification.setOnClickListener { handleVerification() }
    }

    private fun handleEmailSend() {
        val email = binding.etSignupEmail.text.toString()

        if (isEmailValid) {
            // 이메일 인증 코드 전송 요청
            authViewModel.sendVerificationCode(email)

            isEmailSent = true
            binding.etSignupEmail.isEnabled = false
            binding.etSignupEmail.setTextColor(ValidUtils.getTextColorDefault(requireContext()))

            binding.btnSignupSend.isEnabled = false
            binding.btnSignupSend.background = ValidUtils.getButtonInactiveBackground(requireContext())
            binding.tvSignupSend.setTextColor(ValidUtils.getTextColorDefault(requireContext()))

            binding.etSignupVerification.isEnabled = true
            binding.btnSignupVerification.isEnabled = true
            binding.btnSignupVerification.background = ValidUtils.getButtonActiveBackground(requireContext())
            binding.tvSignupVerification.setTextColor(ValidUtils.getTextColorError(requireContext()))

            // ViewModel의 응답에 따라 UI 업데이트
            authViewModel.isVerified.observe(viewLifecycleOwner) { isVerified ->
                if (isVerified) {  // 성공 응답 처리
                    ValidUtils.hideKeyboard(requireContext(), requireView())
                    ValidUtils.showSnackbar(requireView(), "인증 코드가 전송 되었습니다.", binding.lineInfoInput)
                    validateAndProceed() // 상태 업데이트
                } else {
                    ValidUtils.hideKeyboard(requireContext(), requireView())
                    ValidUtils.showSnackbar(requireView(),  "인증 코드를 전송하지 못했습니다.", binding.lineInfoInput)
                }
            }
        }
    }

    private fun handleVerification() {
        val email = binding.etSignupEmail.text.toString()
        val verificationCode = binding.etSignupVerification.text.toString()

        ValidUtils.hideKeyboard(requireContext(), requireView())

        if (isEmailSent) {
            // 이메일 인증 코드 확인 요청
            authViewModel.confirmVerificationCode(email, verificationCode)

            // Viewmodel의 응답에 따라 UI 업데이트
            authViewModel.verifiedUserId.observe(viewLifecycleOwner) { userId ->
                if (userId != null) {  // ID가 존재하면 인증 성공
                    isVerificationValid = true
                    binding.etSignupVerification.isEnabled = false
                    binding.etSignupVerification.setTextColor(ValidUtils.getTextColorDefault(requireContext()))

                    binding.btnSignupVerification.isEnabled = false
                    binding.btnSignupVerification.background = ValidUtils.getButtonInactiveBackground(requireContext())
                    binding.tvSignupVerification.setTextColor(ValidUtils.getTextColorDefault(requireContext()))

                    ValidUtils.showSnackbar(requireView(), "이메일 인증이 완료되었습니다.", binding.lineInfoInput)
                    validateAndProceed() // 상태 업데이트
                } else {
                    ValidUtils.hideKeyboard(requireContext(), requireView())
                    ValidUtils.showSnackbar(requireView(), "올바른 인증 코드를 입력해 주세요.", binding.lineInfoInput)
                }
            }
        }
    }

    private fun setupPasswordValidation() {
        // 비밀번호 입력 시 유효성 검사 수행
        binding.etSignupPassword.addTextChangedListener {
            val password = it.toString()
            isPasswordValid = ValidUtils.isValidPassword(password)
            updatePasswordUI(hasFocus = true) // 입력 중일 때는 항상 유효성만 갱신
            validateAndProceed() // 상태 업데이트
        }

        // 포커스 이동 시 UI 업데이트
        binding.etSignupPassword.setOnFocusChangeListener { _, hasFocus ->
            updatePasswordUI(hasFocus = hasFocus)
        }
    }

    private fun updatePasswordUI(hasFocus: Boolean) {
        // 비밀번호 유효성 검사 결과에 따라 UI 업데이트
        if (isPasswordValid || !hasFocus) {
            binding.tvSignupPasswordHelper.text = "8자~20자 이하, 영대소문자, 숫자, 특수기호 2가지 이상 조합"
            binding.tvSignupPasswordHelper.setTextColor(ValidUtils.getTextColorDefault(requireContext()))
            binding.ivSignupPasswordError.visibility = View.GONE
            binding.etSignupPassword.background = ValidUtils.getBackgroundDefault(requireContext())
        } else {
            binding.tvSignupPasswordHelper.text = "사용 가능한 비밀번호 조합을 입력해 주세요"
            binding.tvSignupPasswordHelper.setTextColor(ValidUtils.getTextColorError(requireContext()))
            binding.ivSignupPasswordError.visibility = View.VISIBLE
            binding.etSignupPassword.background = ValidUtils.getBackgroundError(requireContext())
        }
    }

    private fun setupPasswordMatchValidation() {
        val passwordWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = binding.etSignupPassword.text.toString()
                val confirmPassword = binding.etSignupPasswordConfirm.text.toString()
                isPasswordMatch = password == confirmPassword
                if (binding.etSignupPasswordConfirm.hasFocus()) {
                    updatePasswordMatchUI()
                }
                validateAndProceed() // 상태 갱신
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.etSignupPassword.addTextChangedListener(passwordWatcher)
        binding.etSignupPasswordConfirm.addTextChangedListener(passwordWatcher)
    }

    private fun updatePasswordMatchUI() {
        // 비밀번호 일치 여부에 따라 UI 업데이트
        if (isPasswordMatch) {
            binding.tvSignupConfirmHelper.visibility = View.GONE
            binding.ivSignupConfirmError.visibility = View.GONE
            binding.etSignupPasswordConfirm.background = ValidUtils.getBackgroundDefault(requireContext())
        } else {
            binding.tvSignupConfirmHelper.visibility = View.VISIBLE
            binding.ivSignupConfirmError.visibility = View.VISIBLE
            binding.etSignupPasswordConfirm.background = ValidUtils.getBackgroundError(requireContext())
        }
    }

    private fun validateAndProceed() {
        val nickname = binding.etSignupNickname.text.toString()
        val isNicknameValid = ValidUtils.isValidNickname(nickname)

        // 입력값 상태를 기반으로 버튼 활성화/비활성화
        val isButtonEnabled = isNicknameValid && isPasswordValid && isPasswordMatch && isVerificationValid

        updateNextButtonState(isButtonEnabled)
    }

    // 버튼 활성화/비활성화 상태를 업데이트하는 함수
    private fun updateNextButtonState(isEnabled: Boolean) {
        ValidUtils.updateButtonState(
            binding.btnInfoInputNext,
            binding.tvInfoInputNext,
            binding.ivInfoInputNext,
            isEnabled
        )
    }

    override fun onDestroyView() {
        // 뷰 바인딩 해제
        super.onDestroyView()
        _binding = null
    }
}
