package com.example.hrr_android

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.hrr_android.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.material.bottomnavigation.BottomNavigationView

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding      // 뷰 바인딩
    private var backPressedOnce = false     // 뒤로가기 버튼 상태 저장 변수
    private val handler = Handler(Looper.getMainLooper())   // 시간 초과 처리

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //바텀 네비게이션 세팅
        initBottomNavigation()

        // 시스템 뒤로가기 버튼을 감지해서 두 번 눌렀을 때 종료 실행
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedOnce) {
                    finish() // 앱 종료
                } else {
                    backPressedOnce = true
                    Toast.makeText(this@MainActivity, "\"뒤로\" 버튼 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()

                    // 2초 후 다시 false로 변경하여 재입력 요구
                    handler.postDelayed({ backPressedOnce = false }, 2000)
                }
            }
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

}