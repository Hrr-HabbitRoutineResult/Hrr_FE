package com.example.hrr_android.challenge.certification.ui.draft.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.ItemCertificationDraftBinding

class DraftListAdapter : RecyclerView.Adapter<DraftViewHolder>() {
    private var isEditMode = false
    private val selectedItems = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DraftViewHolder {
        val binding = ItemCertificationDraftBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DraftViewHolder(binding)
    }

    // 선택 개수 변경 리스너
    var onSelectedCountChanged: ((Int) -> Unit)? = null

    fun setEditMode(enabled: Boolean) {
        isEditMode = enabled
        selectedItems.clear()
        onSelectedCountChanged?.invoke(0)  // 편집 모드 진입/해제 시 선택 개수 초기화
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: DraftViewHolder, position: Int) {
        // TODO: 개발용 더미데이터
        holder.binding.apply {
            tvDraftTitle.text = when (position % 3) {
                0 -> "크리스마스의 스터디"
                1 -> "종강 직전이라 80%밖에 못했지만"
                else -> "궁금한 게 있어서 질문드립니다."
            }
            tvDraftDate.text = when (position % 3) {
                0 -> "2024.12.25  14:24"
                1 -> "2024.12.18 09:12"
                else -> "2024.12.18 03:14"
            }
            // 체크박스 처리
            cbDraftSelect.apply {
                visibility = if (isEditMode) View.VISIBLE else View.GONE
                isChecked = selectedItems.contains(position)
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) selectedItems.add(position)
                    else selectedItems.remove(position)
                    // 선택 개수가 변경될 때마다 콜백 호출
                    onSelectedCountChanged?.invoke(selectedItems.size)
                }
            }
        }
    }

    override fun getItemCount() = 3
    fun getSelectedCount() = selectedItems.size

}