package com.example.hrr_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hrr_android.databinding.ActivityLoadingBinding
import com.example.hrr_android.onboarding.model.OnboardingSuccess
import com.example.hrr_android.onboarding.ui.ResultActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoadingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoadingBinding
    private val userViewModel: UserViewModel by viewModels()
    private var selectedCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 온보딩에서 왔는지 확인
        val fromOnboarding = intent.getBooleanExtra("fromOnboarding", false)
        selectedCategory = intent.getStringExtra("selectedCategory")

        if (fromOnboarding && selectedCategory != null) {
            binding.tvLoadingTitle.text = "잠시만 기다려주세요!"
            binding.tvLoadingDescribe.text = "선택한 정보들을 바탕으로 알맞는 챌린지를 찾고 있어요.\n바로 찾아서 알려드릴게요."
            binding.btnOnboardingNext.visibility = View.GONE  // 버튼 숨기기

            startOnboardingRequest(selectedCategory!!)
        } else {
            Log.e("LoadingActivity", "온보딩에서 넘어오지 않았거나 선택된 카테고리가 없습니다.")
            moveToResultActivity(null, "카테고리 선택 오류 발생")
        }
    }

    private fun startOnboardingRequest(category: String) {

        userViewModel.fetchOnboardingChallenge(category)

        userViewModel.onboardingResult.observe(this) { responseList ->
            if (!responseList.isNullOrEmpty()) {
                moveToResultActivity(ArrayList(responseList)) // `List<OnboardingSuccess>` 전달
            } else {
                Log.e("LoadingActivity", "API 응답이 null이거나 비어 있음")
            }
        }

        userViewModel.errorMessage.observe(this) { error ->
            if (error != null) {
                moveToResultActivity(null, error)
            }
        }
    }

    private fun moveToResultActivity(response: ArrayList<OnboardingSuccess>?, errorMessage: String? = null) {
        if (response.isNullOrEmpty() && errorMessage == null) {
            return
        }

        val intent = Intent(this, ResultActivity::class.java).apply {
            putParcelableArrayListExtra("response_data", response)  // List<OnboardingSuccess>를 Parcelable로 전달
            putExtra("error_message", errorMessage)
        }
        startActivity(intent)
        finish()
    }

}
