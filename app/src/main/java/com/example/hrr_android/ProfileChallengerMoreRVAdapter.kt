package com.example.hrr_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.ItemProfileChallengeMoreBinding

class ProfileChallengerMoreRVAdapter(private var challengeList: ArrayList<Challenge>, private var isSaved: Boolean = false): RecyclerView.Adapter<ProfileChallengerMoreRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ProfileChallengerMoreRVAdapter.ViewHolder {
        val binding: ItemProfileChallengeMoreBinding
                = ItemProfileChallengeMoreBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ProfileChallengerMoreRVAdapter.ViewHolder,
        position: Int
    ) {
        val item = challengeList[position]

        holder.bind(item)
    }

    override fun getItemCount(): Int = challengeList.size

    inner class ViewHolder(val binding: ItemProfileChallengeMoreBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(challenge: Challenge){
            binding.tvChallengeMoreDetailTitle.text = challenge.title
            binding.tvChallengeMoreDetailSubtitle.text = challenge.description

            if(isSaved){
                binding.ivChallengeCompleteIcon.visibility = View.INVISIBLE
            }
        }
    }
}