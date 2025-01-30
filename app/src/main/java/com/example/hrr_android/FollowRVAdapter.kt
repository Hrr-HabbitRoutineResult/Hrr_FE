package com.example.hrr_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.ItemProfileFollowBinding

class FollowRVAdapter (private val followList : ArrayList<Follow>) : RecyclerView.Adapter<FollowRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): FollowRVAdapter.ViewHolder {
        val binding: ItemProfileFollowBinding
                = ItemProfileFollowBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = followList[position]

        holder.bind(item)
    }

    override fun getItemCount(): Int = followList.size

    inner class ViewHolder(val binding: ItemProfileFollowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(follow: Follow){
            binding.ivFollowImage.setImageResource(follow.img)
            binding.tvFollowName.text = follow.name
            binding.tvFollowLevel.text = follow.level
            if(follow.isFollowing){
                //내가 팔로우 하는 사용자
                binding.ivFollowerBtn.visibility = View.GONE
                binding.ivFollowingBtn.visibility = View.VISIBLE
            }
            else{
                if(follow.isFollower){
                    //나를 팔로우 하지만, 나는 팔로우 하지 않는 사용자
                    binding.ivFollowerBtn.visibility = View.VISIBLE
                    binding.ivFollowingBtn.visibility = View.GONE
                }
            }

        }
    }
}