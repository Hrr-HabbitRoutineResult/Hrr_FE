package com.example.hrr_android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.ItemProfileBadgeBinding

class ProfileBadgeRVAdapter(private val badgeList : ArrayList<Badge>) : RecyclerView.Adapter<ProfileBadgeRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ProfileBadgeRVAdapter.ViewHolder {
        val binding: ItemProfileBadgeBinding
                = ItemProfileBadgeBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = badgeList[position]

        holder.bind(item)
    }

    override fun getItemCount(): Int = badgeList.size

    inner class ViewHolder(val binding: ItemProfileBadgeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(badge: Badge){
            binding.ivProfileBadgeIcon.setImageResource(badge.icon)
            binding.tvProfileBadgeName.text = badge.name
        }
    }
}