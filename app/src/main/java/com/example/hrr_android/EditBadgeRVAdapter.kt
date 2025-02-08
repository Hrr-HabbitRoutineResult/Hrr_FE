package com.example.hrr_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.ItemBadgeEditBinding

interface OnBadgeClickListener{
    fun onBadgeClick(badge: Badge)
}

class EditBadgeRVAdapter(
    private val badgeList : ArrayList<Badge>,
    private val listener: OnBadgeClickListener,
    private var addPossible: Boolean)
    : RecyclerView.Adapter<EditBadgeRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): EditBadgeRVAdapter.ViewHolder {
        val binding: ItemBadgeEditBinding
                = ItemBadgeEditBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    // addPossible 상태 업데이트
    fun updateAddPossible(newState: Boolean) {
        addPossible = newState // 상태값 변경
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = badgeList[position]

        holder.bind(item)
    }

    override fun getItemCount(): Int = badgeList.size

    inner class ViewHolder(val binding: ItemBadgeEditBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(badge: Badge) {
            val check = binding.ivEditBadgeCheck

            binding.ivEditBadgeIcon.setImageResource(badge.icon)
            binding.tvEditBadgeName.text = badge.name
            if (badge.isSelected) {
                check.visibility = View.VISIBLE
            }

            binding.root.setOnClickListener{
                if(addPossible){
                    // 추가 가능한 상황이면, 체크 상태 반대로
                    if (check.visibility == View.VISIBLE) {
                        check.visibility = View.GONE
                    } else {
                        check.visibility = View.VISIBLE
                    }
                }
                else{
                    // 추가 불가능한 상황 = 대표 뱃지가 이미 3개 선택된 상황에는 체크 해제만 가능하게
                    if (check.visibility == View.VISIBLE) {
                        check.visibility = View.GONE
                    }
                }

                listener.onBadgeClick(badge)
            }
        }
    }

}