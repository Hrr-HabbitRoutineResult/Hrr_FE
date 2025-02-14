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
                tvChallengeTodayTitle.text = item.name
                ivChallengeTodayCategory.setImageResource(getCategoryIcon(item.category))
                tvChallengeTodayDescribe.text = item.description
                tvChallengeTodayParticipateNow.text = item.currentParticipants?.toString() ?: "0"
                tvChallengeTodayParticipateMax.text = item.maxParticipants?.toString() ?: "0"

                // certification_frequency 필드가 Any? 타입이므로 문자열 리스트로 추출
                val certFreqList = extractCertificationFrequencies(item.certificationFrequency)
                // 추출한 값이 있다면 각 코드에 대응하는 텍스트를 얻어 join, 없으면 기타 처리
                val certFreqText = if (certFreqList.isNotEmpty()) {
                    certFreqList.joinToString(", ") { getChallengeFrequency(it) }
                } else {
                    "기타"
                }

                tvChallengeTodayPeriod.text = getChallengeDuration(item.challengeDuration)
                tvChallengeTodayCycle.text = certFreqText
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChallengeTodayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    // String 타입의 인증 주기를 받아 매핑된 한글 텍스트로 변환
    private fun getChallengeFrequency(challengeFrequency: String?): String {
        return when (challengeFrequency) {
            "everyday" -> "매일"
            "week_2" -> "주 2회"
            "week_3" -> "주 3회"
            "week_5" -> "주 5회"
            "weekdays" -> "평일만"
            "weekends" -> "주말만"
            "monday" -> "월"
            "tuesday" -> "화"
            "wednesday" -> "수"
            "thursday" -> "목"
            "friday" -> "금"
            "saturday" -> "토"
            "sunday" -> "일"
            else -> "기타"
        }
    }

    private fun getChallengeDuration(challengeDuration: String?): String {
        return when (challengeDuration) {
            "week_1" -> "1주"
            "week_2" -> "2주"
            "week_3" -> "3주"
            "month_1" -> "1개월"
            "month_3" -> "3개월"
            "month_6" -> "6개월"
            "year_1" -> "1년"
            else -> challengeDuration ?: "기타"
        }
    }

    // certification_frequency 필드가 Any? 타입으로 올 경우, 문자열 또는 문자열 배열에서 String 값만 추출하는 유틸리티 함수
    private fun extractCertificationFrequencies(certificationFrequency: Any?): List<String> {
        return when (certificationFrequency) {
            is String -> listOf(certificationFrequency)
            is List<*> -> certificationFrequency.filterIsInstance<String>()
            else -> emptyList()
        }
    }
}
