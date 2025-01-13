package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hrr_android.databinding.FragmentProfileBinding
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val circularProgressBar = binding.cpbProfileLevelGauge

        // 레벨 달성률 수치
        val requiredPoint = 100
        val earnedPoint = 76

        // 퍼센트 계산
        val progressPercentage = (earnedPoint / requiredPoint.toFloat()) * 50

        // CircularProgressBar 설정 ; 그라데이션, 시간
        circularProgressBar.apply {
            progressBarColorDirection =
                CircularProgressBar.GradientDirection.RIGHT_TO_LEFT
            setProgressWithAnimation(progressPercentage, 1000) // 1초 동안
        }
    }

}