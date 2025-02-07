package com.example.hrr_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.ItemProfileFollowBinding

interface OnFollowClickListener{
    fun onFollowClicked(follow: Follow)
    fun onFollowingClicked(position: Int, follow: Follow)
    fun onUserClicked(follow: Follow)
}

class FollowRVAdapter (
    private val followList : ArrayList<Follow>,
    private val listener: OnFollowClickListener)
    : RecyclerView.Adapter<FollowRVAdapter.ViewHolder>() {
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

            //클릭 이벤트 처리
            binding.llFollowInfo.setOnClickListener {
                // 다른 사람 프로필로 이동
                listener.onUserClicked(follow)
            }
            binding.ivFollowerBtn.setOnClickListener {
                //"팔로우" 상태 아이콘 클릭 시
                binding.ivFollowerBtn.visibility = View.GONE
                binding.ivFollowingBtn.visibility = View.VISIBLE    //팔로우 시작
                listener.onFollowClicked(follow)     //팔로우 정보 제외 처리
            }
            binding.ivFollowingBtn.setOnClickListener {
                //"팔로잉" 상태 아이콘 클릭 시
                listener.onFollowingClicked(bindingAdapterPosition, follow)     //팔로우 정보 처리
            }

        }
    }
}