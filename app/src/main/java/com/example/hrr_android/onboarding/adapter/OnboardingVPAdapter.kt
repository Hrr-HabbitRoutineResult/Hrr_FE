package com.example.hrr_android.onboarding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hrr_android.databinding.ItemChallengeOnboardingBinding
import com.example.hrr_android.onboarding.model.OnboardingSuccess

class OnboardingVPAdapter(private val items: List<OnboardingSuccess>) :
    RecyclerView.Adapter<OnboardingVPAdapter.ChallengeViewHolder>() {

    // 커스텀 ViewHolder 클래스
    class ChallengeViewHolder(private val binding: ItemChallengeOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // 데이터 바인딩
        fun bind(item: OnboardingSuccess) {
            binding.apply {
                tvChallengeOnboardingTitle.text = item.name  // 제목 설정
                tvChallengeOnboardingDescription.text = item.description  // 챌린지 소개 설정
                tvChallengeOnboardingType.text = when (item.type) {
                "basic" -> "베이직"
                "study" -> "스터디"
                else -> "기타"
            }

                // Glide를 사용하여 이미지 로드
                Glide.with(ivChallengeOnboardingCover.context)
                    .load(item.challengeImage)  // 이미지 URL
                    .into(ivChallengeOnboardingCover)
            }
        }
    }

    // ViewHolder 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val binding = ItemChallengeOnboardingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChallengeViewHolder(binding)
    }

    // ViewHolder에 데이터 바인딩
    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    // 아이템 개수 반환 (최대 3개 제한)
    override fun getItemCount(): Int = items.size.coerceAtMost(3)
}
