package com.example.hrr_android.access.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hrr_android.access.ValidUtils
import com.example.hrr_android.access.ui.SignUpActivity
import com.example.hrr_android.databinding.FragmentTermsBinding
import com.google.android.material.snackbar.Snackbar

class TermsFragment : Fragment() {
    private var _binding: FragmentTermsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 뷰 바인딩 초기화
        _binding = FragmentTermsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateNextButtonState()

        // 전체 동의 체크박스 클릭 시 모든 체크박스 상태 변경
        binding.cbAllAgree.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {  // 사용자가 직접 클릭했을 때만 실행하도록 함
                setAllChecked(isChecked)
                updateNextButtonState() // 버튼 상태 업데이트
            }
        }

        // 개별 체크박스 상태 클릭 시 전체 동의 상태 변경
        val checkBoxes = listOf(
            binding.cbServiceTerms,
            binding.cbPrivacyTerms,
            binding.cbThirdParty,
            binding.cbMarketing
        )

        // 체크박스 상태 변경 시 전체 동의 상태 업데이트
        checkBoxes.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, _ ->
                updateAllChecked()
                updateNextButtonState() // 버튼 상태 업데이트
            }
        }

        // 다음 버튼 클릭 시 유효성 검사
        binding.btnTermsNext.setOnClickListener {
            val isKakaoLogin = activity?.intent?.getBooleanExtra("isKakaoLogin", false) ?: false

            val nextFragment = if (isKakaoLogin) {
                NicknameFragment()  // 카카오 로그인 시 닉네임 입력으로 이동
            } else {
                InfoInputFragment()  // 일반 회원가입 시 가입정보 입력으로 이동
            }

            (activity as? SignUpActivity)?.changeFragment(nextFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 전체 동의 체크박스 상태 동기화
    private fun setAllChecked(isChecked: Boolean) {
        binding.cbServiceTerms.isChecked = isChecked
        binding.cbPrivacyTerms.isChecked = isChecked
        binding.cbMarketing.isChecked = isChecked
        binding.cbThirdParty.isChecked = isChecked
        updateNextButtonState() // 버튼 상태 업데이트
    }

    // 개별 체크박스 상태에 따라 전체동의 체크박스 상태 변동
    private fun updateAllChecked() {
        binding.cbAllAgree.isChecked =
            binding.cbServiceTerms.isChecked &&
                    binding.cbPrivacyTerms.isChecked &&
                    binding.cbThirdParty.isChecked &&
                    binding.cbMarketing.isChecked
        updateNextButtonState() // 버튼 상태 업데이트
    }

    // 필수 약관 동의 확인 함수
    private fun isEssentialChecked(): Boolean {
        return binding.cbServiceTerms.isChecked &&
                binding.cbPrivacyTerms.isChecked
    }

    private fun updateNextButtonState() {
        val isEnabled = isEssentialChecked()

        ValidUtils.updateButtonState(
            binding.btnTermsNext,
            binding.tvTermsNext,
            binding.ivTermsNext,
            isEnabled
        )
    }
}