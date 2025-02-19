package com.example.hrr_android.challenge.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.ItemChallengeCertificationBinding

class CertificationListAdapter : RecyclerView.Adapter<CertificationListAdapter.CertificationViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CertificationViewHolder {
        return CertificationViewHolder(
            ItemChallengeCertificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CertificationViewHolder, position: Int) {
        holder.binding.apply {
            ivQuestionBadge.isVisible
        }
    }

    override fun getItemCount(): Int = 5  // 테스트용으로 아이템 5개 표시

    class CertificationViewHolder(
        val binding: ItemChallengeCertificationBinding
    ) : RecyclerView.ViewHolder(binding.root)
}