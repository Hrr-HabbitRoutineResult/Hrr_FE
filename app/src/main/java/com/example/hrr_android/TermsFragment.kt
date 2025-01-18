package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.hrr_android.databinding.FragmentTermsBinding

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

        // 전체 동의 체크박스 클릭 시 모든 체크박스 상태 변경
        binding.cbAllAgree.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {  // 사용자가 직접 클릭했을 때만 실행하도록 함
                setAllChecked(isChecked)
            }
        }

        // 개별 체크박스 상태 클릭 시 전체 동의 상태 변경
        val checkBoxes = listOf(
            binding.cbServiceTerms,
            binding.cbPrivacyTerms,
            binding.cbAgeVerification,
            binding.cbThirdParty,
            binding.cbMarketing
        )

        checkBoxes.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, _ ->
                updateAllChecked()
            }
        }

        // 다음 버튼 클릭 시 유효성 검사
        binding.btnTermsNext.setOnClickListener {
            if (isEssentialChecked()) {
                (activity as? SignUpActivity)?.changeFragment(VerificationFragment())  // 본인인증 프래그먼트로 이동
            } else {
                Toast.makeText(context, "필수 약관에 모두 동의해 주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        binding.btnTermsNext.setOnClickListener(null)
        _binding = null
        super.onDestroyView()
    }

    // 전체 동의 체크박스 상태 동기화
    private fun setAllChecked(isChecked: Boolean) {
        binding.cbServiceTerms.isChecked = isChecked
        binding.cbPrivacyTerms.isChecked = isChecked
        binding.cbAgeVerification.isChecked = isChecked
        binding.cbMarketing.isChecked = isChecked
        binding.cbThirdParty.isChecked = isChecked
    }

    // 개별 체크박스 상태에 따라 전체동의 체크박스 상태 변동
    private fun updateAllChecked() {
        binding.cbAllAgree.isChecked =
            binding.cbServiceTerms.isChecked &&
                    binding.cbPrivacyTerms.isChecked &&
                    binding.cbAgeVerification.isChecked &&
                    binding.cbThirdParty.isChecked &&
                    binding.cbMarketing.isChecked
    }

    // 필수 약관 동의 확인 함수
    private fun isEssentialChecked(): Boolean {
        return binding.cbServiceTerms.isChecked &&
                binding.cbPrivacyTerms.isChecked &&
                binding.cbAgeVerification.isChecked
    }
}