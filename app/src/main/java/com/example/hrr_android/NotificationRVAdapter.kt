package com.example.hrr_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.ItemNotificationListBinding

class NotificationRVAdapter(private val notificationList: List<Notification>) : RecyclerView.Adapter<NotificationRVAdapter.AlarmViewHolder>() {

    // ViewHolder에 뷰 바인딩 적용
    inner class AlarmViewHolder(val binding: ItemNotificationListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        // 뷰 바인딩 초기화
        val binding = ItemNotificationListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = notificationList[position]

        // 동적으로 변환된 텍스트 적용
        holder.binding.ivNotificationCover.setImageResource(alarm.coverImg)  // 알림 아이콘
        holder.binding.tvNotificationTitle.text = alarm.getFormattedTitle()  // 변환된 타이틀
        holder.binding.tvNotificationTime.text = alarm.time  // 알림 시간

        // content가 없으면 숨김
        val contentText = alarm.getFormattedContent()
        if (contentText.isEmpty()) {
            holder.binding.tvNotificationContent.visibility = View.GONE
        } else {
            holder.binding.tvNotificationContent.visibility = View.VISIBLE
            holder.binding.tvNotificationContent.text = contentText
        }

        // 클릭 시 효과 적용
        holder.binding.root.setOnClickListener {
            holder.binding.layoutNotificationList.setBackgroundResource(R.color.white)
            Toast.makeText(holder.binding.root.context, "${alarm.getFormattedTitle()} 클릭됨", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = notificationList.size
}
