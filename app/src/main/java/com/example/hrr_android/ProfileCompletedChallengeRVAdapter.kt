package com.example.hrr_android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.ItemProfileCompletedChallengeBinding

class ProfileCompletedChallengeRVAdapter(private val challengeList : ArrayList<Challenge>) : RecyclerView.Adapter<ProfileCompletedChallengeRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ProfileCompletedChallengeRVAdapter.ViewHolder {
        val binding: ItemProfileCompletedChallengeBinding
                = ItemProfileCompletedChallengeBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = challengeList[position]

        holder.bind(item)
    }

    override fun getItemCount(): Int = challengeList.size

    inner class ViewHolder(val binding: ItemProfileCompletedChallengeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(challenge: Challenge){
            binding.tvProfileCompletedChallengeDetailTitle.text = challenge.title
            binding.tvProfileCompletedChallengeDetailSubtitle.text = challenge.description

        }
    }
}