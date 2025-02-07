package com.example.hrr_android.challenge.ui.record.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.R
import com.example.hrr_android.databinding.ItemRecordTextBinding

class RecordTextAdapter : RecyclerView.Adapter<RecordTextAdapter.RecordTextViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecordTextViewHolder {
        return RecordTextViewHolder(
            ItemRecordTextBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecordTextViewHolder, position: Int) {
        // 더미 데이터 설정
        holder.binding.apply {
            tvRecordTextDate.text = "2024.12.31"
            tvRecordTextTitle.text = "해피뉴이어~! 올해 마지막 인증합니다."
            ivRecordTextThumbnail.setImageResource(R.drawable.img_running)
        }
    }

    override fun getItemCount(): Int = 10  // 테스트용으로 아이템 10개 표시

    class RecordTextViewHolder(
        val binding: ItemRecordTextBinding
    ) : RecyclerView.ViewHolder(binding.root)
}