package com.example.hrr_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.ItemMessageListBinding

class MessageListRVAdapter(
    private val messageList: List<MessageList>,
    private val onItemClick: () -> Unit // 클릭 리스너 추가
) : RecyclerView.Adapter<MessageListRVAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(private val binding: ItemMessageListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: MessageList) {
            binding.ivItemMessageListProfile.setImageResource(message.profileImg)
            binding.tvItemMessageListName.text = message.senderName
            binding.tvItemMessageListContent.text = message.content
            binding.tvItemMessageListTime.text = message.time

            // 아이템 클릭 리스너 설정
            binding.root.setOnClickListener {
                onItemClick()
                binding.ivItemMessageListNotification.visibility = View.GONE // 알림 아이콘 숨기기
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messageList[position])
    }

    override fun getItemCount(): Int = messageList.size
}
