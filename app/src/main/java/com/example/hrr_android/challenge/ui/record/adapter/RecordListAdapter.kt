package com.example.hrr_android.challenge.ui.record.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.R
import com.example.hrr_android.databinding.ItemRecordPhotoBinding

class RecordListAdapter : RecyclerView.Adapter<RecordListAdapter.RecordViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecordViewHolder {
        return RecordViewHolder(
            ItemRecordPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        // 더미 이미지 설정
        holder.binding.ivRecordPhoto.setImageResource(R.drawable.img_record_text_thumbnail)
    }

    override fun getItemCount(): Int = 18  // 테스트용으로 아이템 18개 표시

    class RecordViewHolder(
        val binding: ItemRecordPhotoBinding
    ) : RecyclerView.ViewHolder(binding.root)
}