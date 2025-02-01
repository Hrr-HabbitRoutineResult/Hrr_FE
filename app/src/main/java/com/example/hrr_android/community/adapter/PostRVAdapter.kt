package com.example.hrr_android.community.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.community.model.Post
import com.example.hrr_android.databinding.ItemPostListBinding

class PostRVAdapter(
    private val posts: MutableList<Post>, // Post 데이터를 담는 리스트
    private val onItemClick: (Post) -> Unit, // 아이템 클릭 리스너
    private val fragmentType: String // 프래그먼트 구분값
) : RecyclerView.Adapter<PostRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemPostListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.tvItemPostListTitle.text = post.title
            binding.tvItemPostListContent.text = post.content
            binding.tvItemPostListAuthor.text = post.author
            binding.tvItemPostListLike.text = post.like.toString()
            binding.tvItemPostListComment.text = post.comment.toString()
            binding.tvItemPostListCreated.text = post.createdAt

            // 특정 프래그먼트에서만 tvItemPostListContent 표시
            if (fragmentType == "CommunityTabContentFragment") {
                binding.tvItemPostListCommunity.visibility = View.VISIBLE
                binding.tvItemPostListCommunity.text = post.community
            } else {
                binding.tvItemPostListCommunity.visibility = View.GONE
            }

            // 아이템 클릭 리스너 설정
            binding.root.setOnClickListener {
                onItemClick(post)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPostListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size
}
