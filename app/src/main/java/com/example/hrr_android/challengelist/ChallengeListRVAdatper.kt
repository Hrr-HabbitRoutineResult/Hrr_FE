package com.example.hrr_android.challengelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.R
import com.example.hrr_android.databinding.ItemChallengeBinding
import android.content.Context

class ChallengeListRVAdapter(private var challengeList: List<ChallengeItem> = listOf())
    : RecyclerView.Adapter<ChallengeListRVAdapter.ChallengeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val binding = ItemChallengeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChallengeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val challenge = challengeList[position]
        holder.bind(challenge)

        // 아이템 간 간격
        val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.bottomMargin = dpToPx(holder.itemView.context, 13)
        holder.itemView.layoutParams = layoutParams
    }

    override fun getItemCount(): Int = challengeList.size

    fun updateList(newList: List<ChallengeItem>) {
        challengeList = newList
        notifyDataSetChanged() // 전체 리스트 갱신
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }

    inner class ChallengeViewHolder(private val binding: ItemChallengeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(challenge: ChallengeItem) {
            binding.tvChallengeTitle.text = challenge.title
            binding.tvChallengeDescribe.text = challenge.description
            binding.tvChallengeCycle.text = "${challenge.frequency} | ${challenge.duration}"
            binding.tvChallengePeople.text = "${challenge.currentPeople} / ${challenge.maxPeople}"

            binding.icChallengeStudy.visibility = if (challenge.type) View.GONE else View.VISIBLE
            binding.ivChallengeWrite.visibility = if (challenge.authType) View.VISIBLE else View.GONE
            binding.ivChallengeCamera.visibility = if (challenge.authType) View.GONE else View.VISIBLE

            binding.root.background = ContextCompat.getDrawable(binding.root.context, R.drawable.bg_item_challenge)

            val greyColor = ContextCompat.getColor(binding.root.context, R.color.grey_500)
            val defaultColor = ContextCompat.getColor(binding.root.context, R.color.text_primary)

            // ✅ 기본 상태 초기화
            binding.tvChallengeTitle.setTextColor(defaultColor)
            binding.tvChallengeDescribe.setTextColor(defaultColor)
            binding.tvChallengeCycle.setTextColor(defaultColor)
            binding.tvChallengePeople.setTextColor(defaultColor)

            binding.icChallengeStudy.setImageResource(R.drawable.ic_challenge_study)
            binding.ivChallengeWrite.clearColorFilter()
            binding.ivChallengeCamera.clearColorFilter()
            binding.icChallengePerson.clearColorFilter()

            // ✅ 참여 인원이 찼을 경우 (회색 적용)
            if (challenge.currentPeople == challenge.maxPeople) {
                binding.tvChallengeTitle.setTextColor(greyColor)
                binding.tvChallengeDescribe.setTextColor(greyColor)
                binding.tvChallengeCycle.setTextColor(greyColor)
                binding.tvChallengePeople.setTextColor(greyColor)

                binding.icChallengeStudy.setImageResource(R.drawable.ic_challenge_study_grey)
                binding.ivChallengeWrite.setColorFilter(greyColor)
                binding.ivChallengeCamera.setColorFilter(greyColor)
                binding.icChallengePerson.setColorFilter(greyColor)
            }
        }
    }

}
