package com.example.hrr_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hrr_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()

    }

    private fun initBottomNavigation(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBottomNavi.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.navi_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, HomeFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }

                R.id.navi_community -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, MakeChallengeFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }
                R.id.navi_message -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, MessageFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }
                R.id.navi_profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, ProfileFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
}