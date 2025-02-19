package com.example.hrr_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.ItemPostCommentedBinding

class CommentedPostRVAdapter(private var posts: ArrayList<Post>): RecyclerView.Adapter<CommentedPostRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): CommentedPostRVAdapter.ViewHolder{
        val binding: ItemPostCommentedBinding
                = ItemPostCommentedBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CommentedPostRVAdapter.ViewHolder,
        position: Int
    ) {
        val item = posts[position]

        holder.bind(item)
    }

    override fun getItemCount(): Int = posts.size

    inner class ViewHolder(val binding: ItemPostCommentedBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(post: Post){
            binding.tvPostRecordContentName.text = post.challengeName
            binding.tvPostRecordContentTitle.text = post.postTitle
            binding.tvPostRecordContentDate.text = post.date

            if(post.coverimg == null){
                //커버 이미지 없을 때
                binding.ivPostRecordContentImg.visibility = View.INVISIBLE
            }
            else{
                //커버 이미지 있을 때
                binding.ivPostRecordContentImg.setImageResource(post.coverimg!!)
            }

            if(!post.hasLink){
                //링크 없을 때
                binding.ivPostRecordLink.visibility = View.INVISIBLE
            }

            if(post.postTitle==""){
                //게시글 제목 없을 때
                binding.llPostTitleAndLink.visibility = View.GONE
            }
        }
    }
}