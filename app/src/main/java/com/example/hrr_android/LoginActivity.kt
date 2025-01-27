package com.example.hrr_android

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hrr_android.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로그인 버튼 클릭 리스너
        binding.btnLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString().trim()
            val password = binding.etLoginPassword.text.toString().trim()

            // 유효성 검사 실행
            if (isValidInput(email, password)) {
                Toast.makeText(this, "유효성 검사 완료", Toast.LENGTH_SHORT).show()
                // MainActivity로 화면 전환 (추후에 로그인 API 호출)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // 회원가입 클릭 시 SignUpActivity로 이동
        binding.tvLoginSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // 비밀번호 찾기 클릭 시 FindInfoActivity로 이동
        binding.tvLoginFindPassword.setOnClickListener {
            val intent = Intent(this, FindInfoActivity::class.java)
            startActivity(intent)
        }
    }

    // 이메일 및 비밀번호 유효성 검사 함수
    private fun isValidInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            Toast.makeText(this, "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "유효한 이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}
