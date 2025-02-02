package com.example.hrr_android.access.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.hrr_android.MainActivity
import com.example.hrr_android.access.PasswordNavigator
import com.example.hrr_android.databinding.ActivityLoginBinding
import androidx.activity.viewModels
import com.example.hrr_android.access.AuthViewModel
import com.example.hrr_android.access.ValidUtils
import com.example.hrr_android.databinding.CustomSnackbarBinding
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()  // 뷰 모델 초기화

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로그인 버튼 클릭 리스너
        binding.btnLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString().trim()
            val password = binding.etLoginPassword.text.toString().trim()

            authViewModel.login(email, password)  // 로그인 API 호출
        }

        authViewModel.loginResult.observe(this) { result ->
            result.onSuccess {
                moveToMainActivity()
            }.onFailure {
                ValidUtils.hideKeyboard(this, binding.root)
                showCustomSnackbar(binding.root)
            }
        }

        // 회원가입 클릭 시 SignUpActivity로 이동
        binding.tvLoginSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // 비밀번호 찾기 클릭 시
        binding.tvLoginFindPassword.setOnClickListener {
            navigateToPasswordActivity(PasswordNavigator.VERIFICATION)
        }

        // 네이버 로고 클릭 시
        binding.ivLoginNaver.setOnClickListener {
            navigateToPasswordActivity(PasswordNavigator.RESET)
        }
    }

    // 로그인 성공 시 MainActivity로 이동
    private fun moveToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToPasswordActivity(fragment: PasswordNavigator) {
        val intent = Intent(this, PasswordActivity::class.java)
        intent.putExtra("fragment_to_load", fragment.fragmentName) // Enum에서 fragmentName 사용
        startActivity(intent)
    }

    // 커스텀 스낵바
    private fun showCustomSnackbar(view: View) {
        val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG) // 빈 텍스트로 기본 Snackbar 생성

        // Snackbar의 뷰 가져오기
        val snackbarView = snackbar.view as ViewGroup
        val context = snackbarView.context

        // 기존 Snackbar
        val defaultTextView = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        defaultTextView.visibility = View.INVISIBLE

        // 스낵바 커스텀
        val binding = CustomSnackbarBinding.inflate(LayoutInflater.from(context), snackbarView, false)
        binding.tvSnackbarContent.text = "이메일과 비밀번호를 다시 확인해주세요"
        snackbarView.setBackgroundColor(Color.TRANSPARENT)
        snackbarView.setPadding(0, 0, 0, 0)
        snackbarView.addView(binding.root)

        snackbar.show()
    }

}
