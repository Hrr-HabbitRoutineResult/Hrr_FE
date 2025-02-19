package com.example.hrr_android.access.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.hrr_android.MainActivity
import com.example.hrr_android.access.PasswordNavigator
import com.example.hrr_android.databinding.ActivityLoginBinding
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.hrr_android.access.AuthEventManager
import com.example.hrr_android.access.AuthViewModel
import com.example.hrr_android.access.ValidUtils
import com.example.hrr_android.databinding.CustomSnackbarBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()  // 뷰 모델 초기화
    private var backPressedOnce = false     // 뒤로가기 버튼 상태 저장 변수
    private val handler = Handler(Looper.getMainLooper())   // 시간 초과 처리

    private var logoutObserver: Observer<Unit>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // AuthEventManager 로그아웃 이벤트 Observer 등록 (만약 등록되어 있다면)
        logoutObserver = Observer {
            Log.d("LoginActivity", "로그아웃 이벤트 수신됨")
            // 이미 로그인 중이면 별도의 처리가 필요하지 않음
        }
        AuthEventManager.logoutEvent.observe(this, logoutObserver!!)

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

        binding.ivLoginKakao.setOnClickListener {
            attemptKakaoLogin()
        }

        // 카카오 로그인 결과 관찰
        authViewModel.kakaoLoginResult.observe(this) { result ->
            result.onSuccess {
                Log.d("KakaoLogin", "로그인 성공! JWT")
                // Intent에 카카오 로그인 여부를 담아서 전달
                val intent = Intent(this, SignUpActivity::class.java)
                intent.putExtra("isKakaoLogin", true)  // 카카오 로그인 여부 전달
                startActivity(intent)
            }.onFailure { error ->
                Log.e("KakaoLogin", "로그인 실패: ${error.message}")
            }
        }

        // 시스템 뒤로가기 버튼을 감지해서 두 번 눌렀을 때 종료 실행
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedOnce) {
                    finish() // 앱 종료
                } else {
                    backPressedOnce = true
                    Toast.makeText(this@LoginActivity, "\"뒤로\" 버튼 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()

                    // 2초 후 다시 false로 변경하여 재입력 요구
                    handler.postDelayed({ backPressedOnce = false }, 2000)
                }
            }
        })
    }

    // 로그인 성공 시 MainActivity로 이동
    private fun moveToMainActivity() {
        // Observer 제거
        logoutObserver?.let { AuthEventManager.logoutEvent.removeObserver(it) }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
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
        binding.tvSnackbarContent.text = "로그인에 실패하였습니다."
        snackbarView.setBackgroundColor(Color.TRANSPARENT)
        snackbarView.setPadding(0, 0, 0, 0)
        snackbarView.addView(binding.root)

        snackbar.show()
    }

    // 카카오 로그인 시도
    private fun attemptKakaoLogin() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            // 카카오톡 로그인
            loginWithKakaoTalk()
        } else {
            // 카카오 계정 로그인
            loginWithKakaoAccount()
        }
    }

    // 카카오톡으로 로그인
    private fun loginWithKakaoTalk() {
        UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
            if (error != null) {
                Log.e("KakaoLogin", "카카오톡 로그인 실패", error)
                loginWithKakaoAccount() // 카카오톡 로그인 실패 시 계정 로그인으로 시도
            } else if (token != null) {
                Log.i("KakaoLogin", "카카오톡 로그인 성공")
                handleLoginSuccess(token)
            }
        }
    }

    // 카카오 계정으로 로그인
    private fun loginWithKakaoAccount() {
        UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
            if (error != null) {
                Log.e("KakaoLogin", "카카오 계정 로그인 실패", error)
            } else if (token != null) {
                Log.i("KakaoLogin", "카카오 계정 로그인 성공")
                handleLoginSuccess(token)
            }
        }
    }

    // 카카오 로그인 성공 시 서버에 AccessToken 전달
    private fun handleLoginSuccess(token: OAuthToken) {
        val kakaoAccessToken = token.accessToken
        Log.d("KakaoLogin", "카카오 로그인 성공, Access Token: $kakaoAccessToken")
        // ViewModel을 통해 로그인 요청을 보냄
        authViewModel.loginWithKakao(token.accessToken)
    }
}
