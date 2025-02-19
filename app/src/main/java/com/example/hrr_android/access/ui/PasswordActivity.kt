package com.example.hrr_android.access.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hrr_android.access.PasswordNavigator
import com.example.hrr_android.access.ui.fragment.PasswordResetFragment
import com.example.hrr_android.R
import com.example.hrr_android.access.ui.fragment.VerificationFragment
import com.example.hrr_android.databinding.ActivityPasswordBinding

class PasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뒤로 가기
        binding.btnPasswordBack.setOnClickListener {
            finish()
        }

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
}

