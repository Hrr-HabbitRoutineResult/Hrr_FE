package com.example.hrr_android.access.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.hrr_android.DialogNoTitle
import com.example.hrr_android.MainActivity
import com.example.hrr_android.access.PasswordNavigator
import com.example.hrr_android.access.ui.fragment.PasswordResetFragment
import com.example.hrr_android.R
import com.example.hrr_android.access.ui.fragment.VerificationFragment
import com.example.hrr_android.databinding.ActivityPasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 앱의 뒤로 가기 버튼 동작
        binding.btnPasswordBack.setOnClickListener {
            handleBackPressed()
        }

        // 휴대폰 자체의 뒤로 가기 동작
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPressed()
            }
        })

        // Intent로부터 전달받은 fragmentName을 기반으로 PasswordFragment를 가져옴
        val fragmentName = intent.getStringExtra("fragment_to_load")
        val selectedFragment = PasswordNavigator.fromFragmentName(fragmentName)

        // 제목 설정
        binding.tvPasswordRecoveryTitle.text = selectedFragment.title

        // Fragment 로드
        val fragment = when (selectedFragment) {
            PasswordNavigator.VERIFICATION -> VerificationFragment()
            PasswordNavigator.RESET -> PasswordResetFragment()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_password_fragment, fragment)
            .commit()
    }

    private fun handleBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.layout_password_fragment)

        when (currentFragment) {
            is VerificationFragment -> {
                // VerificationFragment에서 뒤로 가기
                val dialog = DialogNoTitle(
                    context = this,
                    message = "진행 중인 작업을 중단하고 로그인 화면으로 이동하시겠습니까?",
                    yesText = "네",
                    noText = "아니오",
                    object : DialogNoTitle.DialogListener {
                        override fun onYesClicked() {
                            val intent = Intent(this@PasswordActivity, LoginActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            startActivity(intent)
                            finish()
                        }

                        override fun onNoClicked() {
                            // 다이얼로그 닫기
                        }
                    }
                )
                dialog.show()
            }

            is PasswordResetFragment -> {
                // PasswordResetFragment에서 뒤로 가기
                val dialog = DialogNoTitle(
                    context = this,
                    message = "진행 중인 작업을 중단하고 홈화면으로 이동하시겠습니까?",
                    yesText = "네",
                    noText = "아니오",
                    object : DialogNoTitle.DialogListener {
                        override fun onYesClicked() {
                            val intent = Intent(this@PasswordActivity, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            startActivity(intent)
                            finish()
                        }

                        override fun onNoClicked() {
                            // 다이얼로그 닫기
                        }
                    }
                )
                dialog.show()
            }
        }
    }

}

