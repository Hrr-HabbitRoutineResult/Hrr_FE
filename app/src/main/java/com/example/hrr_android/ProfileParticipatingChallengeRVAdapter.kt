package com.example.hrr_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.ItemProfileParticipatingChallengeBinding

class ProfileParticipatingChallengeRVAdapter(private val challengeList : ArrayList<Challenge>) : RecyclerView.Adapter<ProfileParticipatingChallengeRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ProfileParticipatingChallengeRVAdapter.ViewHolder {
        val binding: ItemProfileParticipatingChallengeBinding
        = ItemProfileParticipatingChallengeBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileParticipatingChallengeRVAdapter.ViewHolder, position: Int) {
        val item = challengeList[position]

        holder.bind(item)
    }

    override fun getItemCount(): Int = challengeList.size

    inner class ViewHolder(val binding: ItemProfileParticipatingChallengeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(challenge: Challenge){
            binding.ivProfileParticipatingChallenge.setImageResource(challenge.coverimg)
            binding.tvProfileParticipatingChallengeTitle.text = challenge.title
            if(challenge.isCertified){
                binding.ivProfileParticipatingChallengeUnchecked.visibility = View.INVISIBLE
                binding.ivProfileParticipatingChallengeChecked.visibility = View.VISIBLE
            }
            else{
                binding.ivProfileParticipatingChallengeUnchecked.visibility = View.VISIBLE
                binding.ivProfileParticipatingChallengeChecked.visibility = View.INVISIBLE
            }

        }
    }
}