package com.example.hrr_android

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.example.hrr_android.access.AuthViewModel
import com.example.hrr_android.access.ui.LoginActivity
import com.example.hrr_android.databinding.BottomSheetWithdrawalBinding
import com.example.hrr_android.databinding.DialogBottomSheetBinding
import com.example.hrr_android.databinding.DialogLogoutBinding
import com.example.hrr_android.databinding.FragmentSettingAccountBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingAccountFragment : Fragment() {
    private var _binding: FragmentSettingAccountBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingAccountBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListener()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (parentFragmentManager.backStackEntryCount >= 1) {
                        parentFragmentManager.popBackStack() // 추가 Fragment가 있을 때느 이전 Fragment로 돌아감

                        // 가장 최근에 추가된 Fragment를 다시 보이도록 설정
                        val currentFragment = parentFragmentManager.fragments.lastOrNull()
                        currentFragment?.let {
                            parentFragmentManager.beginTransaction().show(it).commit()
                        }
                    }

                    (activity as? ProfileMoreActivity)?.setTitle("설정")
                }
            })

        userViewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                val errorToUser = when {
                    it.contains("IllegalStateException") -> "데이터를 불러오는 중 문제가 발생했습니다. 다시 시도해 주세요."
                    it.contains("JsonSyntaxException") -> "서버 응답이 올바르지 않습니다. 업데이트를 확인해 주세요."
                    it.contains("SocketTimeoutException") -> "서버 응답이 지연되고 있습니다. 잠시 후 다시 시도해 주세요."
                    it.contains("IOException") -> "네트워크 연결을 확인해 주세요."
                    else -> "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."
                }

                Toast.makeText(requireContext(), errorToUser, Toast.LENGTH_LONG).show()
                Log.e("ProfileFragmentVM", "오류 발생: $errorMsg")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? ProfileMoreActivity)?.setTitle("설정")
        _binding = null
    }

    private fun initClickListener() {
        // 로그아웃 처리
        binding.llSettingLogout.setOnClickListener {
            showLinkDialog()
        }

        // 탈퇴 다이얼로그
        binding.llSettingWithdrawal.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
            val dialogBinding = BottomSheetWithdrawalBinding.inflate(layoutInflater) // 뷰 바인딩 객체 생성

            dialog.setContentView(dialogBinding.root)

            // 버튼 클릭 리스너 설정
            dialogBinding.llWithdrawalTitle.setOnClickListener {
                dialog.dismiss()
            }

            dialogBinding.btnNoWithdrawal.setOnClickListener {
                dialog.dismiss()
            }

            dialogBinding.btnWithdrawal.setOnClickListener {
                userViewModel.withdrawal()  // 회원 탈퇴
                moveToLoginActivity()
                dialog.dismiss()
            }

            dialog.show()
        }


    }

    private fun showLinkDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_logout, null)

        val logoutBinding = DialogLogoutBinding.bind(dialogView) // 뷰 바인딩 사용

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        logoutBinding.tvLogoutYes.setOnClickListener {
            logout()
            dialog.dismiss()
        }

        logoutBinding.tvLogoutNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun logout() {
        authViewModel.logout() // ViewModel에서 로그아웃 처리
        moveToLoginActivity() // 로그인 화면으로 이동
    }

    private fun moveToLoginActivity() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish() // 현재 액티비티 종료
    }
}