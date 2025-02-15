package com.example.hrr_android.challenge.certification.ui.post.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.ItemCommentBinding

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(private val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                tvCommentName.text = when (position % 3) {
                    0 -> "작성자명1"
                    1 -> "작성자명2"
                    else -> "작성자명3"
                }

                tvCommentContent.text = when (position % 3) {
                    0 -> "Text1"
                    1 -> "Text2"
                    else -> "Text3"
                }

                tvCommentTime.text = when (position % 3) {
                    0 -> "방금 전"
                    1 -> "1시간 전"
                    else -> "2시간 전"
                }

                // 버튼 클릭 리스너 설정
                btnCommentAccept.setOnClickListener {
                    // 채택 버튼 클릭 처리
                }

                btnCommentReply.setOnClickListener {
                    // 답글 버튼 클릭 처리
                }

                btnCommentLike.setOnClickListener {
                    // 좋아요 버튼 클릭 처리
                }

                btnCommentMore.setOnClickListener {
                    // 더보기 버튼 클릭 처리
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = 10  // 테스트용
}