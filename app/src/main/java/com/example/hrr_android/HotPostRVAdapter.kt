package com.example.hrr_android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.ItemHotPostListBinding

class HotPostRVAdapter(private val hotPost: List<HotPost>) : RecyclerView.Adapter<HotPostRVAdapter.HotPostViewHolder>() {

    // ViewHolder에 뷰 바인딩 적용
    inner class HotPostViewHolder(val binding: ItemHotPostListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotPostViewHolder {
        // 뷰 바인딩 초기화
        val binding = ItemHotPostListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HotPostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HotPostViewHolder, position: Int) {
        val hotPost = hotPost[position]

        // 바인딩으로 뷰 연결
        holder.binding.tvHotPostListCommunity.text = hotPost.community // 게시판 텍스트 설정
        holder.binding.tvHotPostListContent.text = hotPost.content  // 인기글 텍스트 설정
    }

    override fun getItemCount(): Int = hotPost.size
}
