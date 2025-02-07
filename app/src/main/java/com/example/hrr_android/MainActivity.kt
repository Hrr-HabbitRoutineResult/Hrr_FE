package com.example.hrr_android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.hrr_android.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    //뷰 바인딩
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //바텀 네비게이션 세팅
        initBottomNavigation()

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

    // 프래그먼트에서 접근할 수 있도록 추가
    fun getBinding(): ActivityMainBinding {
        return binding
    }
}