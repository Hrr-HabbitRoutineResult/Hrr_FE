package com.example.hrr_android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.ItemChallengeTodayBinding

class ChallengeTodayVPAdapter(private val challengeList: List<ChallengeToday>) :
    RecyclerView.Adapter<ChallengeTodayVPAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemChallengeTodayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChallengeToday) {
            with(binding) {
                tvChallengeTodayTitle.text = item.title
                layoutChallengeTodayCategory.setImageResource(item.categoryIconRes)
                tvChallengeTodayDescribe.text = item.description
                tvChallengeTodayParticipateNow.text = item.participantsNow.toString()
                tvChallengeTodayParticipateMax.text = item.participantsMax.toString()
                tvChallengeTodayCycle.text = item.cycle
                tvChallengeTodayPeriod.text = item.period
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemChallengeTodayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (challengeList.isNotEmpty()) {
            // 실제 데이터 개수의 배수를 사용하여 양 끝 이동 적용
            val realPosition = position % challengeList.size
            holder.bind(challengeList[realPosition])
        }
    }

    override fun getItemCount(): Int {
        // 데이터 개수의 2배를 반환하여 양 끝 이동이 가능하도록 설정
        return if (challengeList.isNotEmpty()) challengeList.size * 2 else 0
    }
}
