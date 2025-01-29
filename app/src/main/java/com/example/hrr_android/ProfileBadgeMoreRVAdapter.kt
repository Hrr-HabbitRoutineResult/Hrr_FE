package com.example.hrr_android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.ItemProfileBadgeMoreBinding

class ProfileBadgeMoreRVAdapter(private val badgeList : ArrayList<Badge>) : RecyclerView.Adapter<ProfileBadgeMoreRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ProfileBadgeMoreRVAdapter.ViewHolder {
        val binding: ItemProfileBadgeMoreBinding
                = ItemProfileBadgeMoreBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = badgeList[position]

        holder.bind(item)
    }

    override fun getItemCount(): Int = badgeList.size

    inner class ViewHolder(val binding: ItemProfileBadgeMoreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(badge: Badge){
            binding.includeItemProfileBadge.ivProfileBadgeIcon.setImageResource(badge.icon)
            binding.includeItemProfileBadge.tvProfileBadgeName.text = badge.name
        }
    }
}