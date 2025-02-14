package com.example.hrr_android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.ItemChallengeTodayBinding

class ChallengeTodayVPAdapter(private val challengeList: Result<List<ChallengeHotness>>) :
    RecyclerView.Adapter<ChallengeTodayVPAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemChallengeTodayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChallengeHotness) {
            with(binding) {
                tvChallengeTodayTitle.text = item.challenge.name
                ivChallengeTodayCategory.setImageResource(getCategoryIcon(item.challenge.category))
                tvChallengeTodayDescribe.text = item.challenge.description
                tvChallengeTodayParticipateNow.text = item.challenge.currentParticipants?.toString() ?: "0"
                tvChallengeTodayParticipateMax.text = item.challenge.maxParticipants?.toString() ?: "0"
                tvChallengeTodayCycle.text = item.challenge.updatedAt ?: "0"
                tvChallengeTodayPeriod.text = item.challenge.duration ?: "0"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemChallengeTodayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        challengeList.onSuccess { list ->
            if (list.isNotEmpty()) {
                // 실제 데이터 개수의 배수를 사용하여 양 끝 이동 적용
                val realPosition = position % list.size
                holder.bind(list[realPosition])
            }
        }
    }

    override fun getItemCount(): Int {
        return challengeList.getOrNull()?.let { it.size * 2 } ?: 0
    }

    private fun getCategoryIcon(category: String?): Int {
        return when (category) {
            "exercise" -> R.drawable.ic_challenge_today_health
            "study" -> R.drawable.ic_challenge_today_learn
            "lifestyle" -> R.drawable.ic_challenge_today_routine
            "hobby" -> R.drawable.ic_challenge_today_hobby
            "jobPreparation" -> R.drawable.ic_challenge_today_company
            else -> R.drawable.ic_challenge_today_health
        }
    }
}
