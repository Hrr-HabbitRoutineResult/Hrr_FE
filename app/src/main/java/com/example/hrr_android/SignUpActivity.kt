package com.example.hrr_android

import android.os.Bundle
import android.text.TextUtils.replace
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.hrr_android.databinding.ActivitySignUpBinding


class SignUpActivity : AppCompatActivity() {

    // 뷰 바인딩 선언
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰 바인딩 초기화
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 프래그먼트 로드
        if (savedInstanceState == null) {
            loadFragment(InfoInputFragment())
        }
    }

    // 프래그먼트 로드 함수
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }
}