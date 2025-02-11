package com.example.hrr_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.hrr_android.access.AuthViewModel
import com.example.hrr_android.access.ui.LoginActivity
import com.example.hrr_android.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.material.bottomnavigation.BottomNavigationView

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //뷰 바인딩
    private lateinit var binding: ActivityMainBinding

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //바텀 네비게이션 세팅
        initBottomNavigation()

        // logoutEvent를 관찰하여 로그아웃 이벤트 발생 시 LoginActivity로 전환
        authViewModel.logoutEvent.observe(this, Observer {
            // 로그아웃 이벤트 수신 시 호출되는 콜백
            navigateToLoginActivity()
        })
    }

    private fun initBottomNavigation(){

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_frame) as? NavHostFragment
        val navController = navHostFragment?.navController

        if (navController == null) {
            Log.e("NavDebug", "NavController is NULL! Check if main_frame is a NavHostFragment")
            return

        }
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.main_bottom_navi)
        bottomNavigationView.setupWithNavController(navController)

    }

    // 로그인 화면으로 전환하는 함수
    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    // 프래그먼트에서 접근할 수 있도록 추가
    fun getBinding(): ActivityMainBinding {
        return binding
    }
}