package com.example.hrr_android.community.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.community.model.Community
import com.example.hrr_android.databinding.ItemCommunityBinding

class CommunityRVAdapter(
    private val items: MutableList<Community>, // MutableList로 선언
    private val onItemClick: (Community) -> Unit // 클릭 리스너 추가
) : RecyclerView.Adapter<CommunityRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCommunityBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCommunityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        with(holder.binding) {
            tvItemCommunityTitle.text = item.title
            tvItemCommunityDescribe.text = item.description

            // 설명이 없을 경우 TextView를 GONE 처리
            tvItemCommunityDescribe.visibility = if (item.description.isEmpty()) View.GONE else View.VISIBLE

            // 핀 여부에 따라 아이콘 설정
            ivCommunityItemPinSelected.visibility = if (item.isPinned) View.VISIBLE else View.GONE
            ivCommunityItemPinUnselected.visibility = if (item.isPinned) View.GONE else View.VISIBLE

            // 핀 아이콘 클릭 이벤트
            ivCommunityItemPinSelected.setOnClickListener {
                items[position] = item.copy(isPinned = false) // 새 객체로 교체
                notifyItemChanged(position) // UI 업데이트
            }

            ivCommunityItemPinUnselected.setOnClickListener {
                items[position] = item.copy(isPinned = true) // 새 객체로 교체
                notifyItemChanged(position) // UI 업데이트
            }

            // 아이템 클릭 이벤트
            root.setOnClickListener {
                onItemClick(item) // 클릭된 아이템을 전달
            }
        }
    }

    override fun getItemCount(): Int = items.size
}