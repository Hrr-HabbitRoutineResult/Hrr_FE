package com.example.hrr_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hrr_android.databinding.ActivityMyBadgeDetailBinding

class MyBadgeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyBadgeDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //뷰 바인딩 초기화
        binding = ActivityMyBadgeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}