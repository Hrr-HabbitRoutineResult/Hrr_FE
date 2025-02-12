import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hrr_android.ChallengesOngoing
import com.example.hrr_android.databinding.ItemChallengeCardDefaultBinding
import com.example.hrr_android.databinding.ItemChallengeCardMoreBinding

class ChallengeCardVPAdapter(private val items: List<ChallengesOngoing>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_NORMAL = 0  // ItemChallengeCardDefault를 나타내는 뷰 타입
        private const val VIEW_TYPE_EMPTY = 1  // ItemChallengeCardNone를 나타내는 뷰 타입
        private const val MAX_ITEMS = 5  // 최대 아이템 수 제한
    }

    inner class ChallengeViewHolder(private val binding: ItemChallengeCardDefaultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChallengesOngoing) {
            binding.apply {
                tvChallengeCardTitle.text = item.name  // 챌린지 제목 설정
                Glide.with(root.context).load(item.image).into(ivChallengeCardCover)  // 챌린지 이미지 설정

                // 챌린지 타입 설정
                tvChallengeCardType.text = when (item.type) {
                    "basic" -> "베이직"
                    "study" -> "스터디"
                    else -> "기타"
                }

                // 인증 상태 아이콘 표시
                if (item.verification) {
                    icChallengeCardChecked.visibility = View.VISIBLE
                    icChallengeCardUnchecked.visibility = View.GONE
                } else {
                    icChallengeCardChecked.visibility = View.GONE
                    icChallengeCardUnchecked.visibility = View.VISIBLE
                }
            }
        }
    }

    inner class EmptyViewHolder(binding: ItemChallengeCardMoreBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (position < items.size) VIEW_TYPE_NORMAL else VIEW_TYPE_EMPTY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_NORMAL) {
            // Default 아이템의 뷰 홀더 생성
            val binding = ItemChallengeCardDefaultBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            ChallengeViewHolder(binding)
        } else {
            // More 아이템의 뷰 홀더 생성
            val binding = ItemChallengeCardMoreBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            EmptyViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ChallengeViewHolder && position < items.size) {
            holder.bind(items[position])
        }
    }

    override fun getItemCount(): Int {  // 아이템이 5 이하면 None 뷰 추가, 그렇지 않으면 5개까지만 표시
        return if (items.size < MAX_ITEMS) items.size + 1 else MAX_ITEMS
    }
}
