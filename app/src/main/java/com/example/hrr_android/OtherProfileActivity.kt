package com.example.hrr_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hrr_android.databinding.ActivityOtherProfileBinding

class OtherProfileActivity : AppCompatActivity() {
    //뷰 바인딩
    private lateinit var binding: ActivityOtherProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOtherProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}