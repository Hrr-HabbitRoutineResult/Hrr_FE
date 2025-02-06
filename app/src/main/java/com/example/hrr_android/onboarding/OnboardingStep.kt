package com.example.hrr_android.onboarding

import androidx.fragment.app.Fragment
import com.example.hrr_android.onboarding.ui.fragment.CategoryFragment
import com.example.hrr_android.onboarding.ui.fragment.GoalFragment
import com.example.hrr_android.onboarding.ui.fragment.InfoSelectFragment

enum class OnboardingStep(val progress: Int, val title: String, val description: String) {
    INFO_SELECT(33, "안녕하세요!", "아래에서 본인의 특성을 선택해주세요."),
    CATEGORY(66, "관심 있는 분야가 무엇인가요?", "아래의 카테고리에서 가장 관심있는 분야를 선택해주세요."),
    GOAL(100, "나의 목표는...", "아래에서 챌린지를 통해 이루고 싶은 목표를 선택해주세요. (최대 3개)");

    companion object {
        fun fromFragment(fragment: Fragment): OnboardingStep? {
            return when (fragment) {
                is InfoSelectFragment -> INFO_SELECT
                is CategoryFragment -> CATEGORY
                is GoalFragment -> GOAL
                else -> null
            }
        }
    }
}