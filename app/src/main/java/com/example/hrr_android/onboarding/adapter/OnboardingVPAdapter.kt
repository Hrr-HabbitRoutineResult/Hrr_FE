package com.example.hrr_android.onboarding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.Challenge
import com.example.hrr_android.databinding.ItemChallengeOnboardingBinding

class OnboardingVPAdapter(private val items: List<Challenge>) :
    RecyclerView.Adapter<OnboardingVPAdapter.ChallengeViewHolder>() {

    // 커스텀 ViewHolder 클래스
    class ChallengeViewHolder(private val binding: ItemChallengeOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // 데이터 바인딩
        fun bind(item: Challenge) {
            binding.apply {
                tvChallengeOnboardingTitle.text = item.title  // 제목 설정
                ivChallengeOnboardingCover.setImageResource(item.coverimg)  // 커버 이미지 설정
                tvChallengeOnboardingDescription.text = item.description  // 챌린지 소개 설정
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

    // 아이템 개수 반환
    override fun getItemCount(): Int = items.size
}
