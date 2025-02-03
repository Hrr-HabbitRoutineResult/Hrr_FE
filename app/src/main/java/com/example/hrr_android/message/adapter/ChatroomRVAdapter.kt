package com.example.hrr_android.message.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.message.model.ChatMessage
import com.example.hrr_android.databinding.ItemMessageBinding
import com.example.hrr_android.databinding.ItemMessageOtherBinding

class ChatroomRVAdapter(private val chatList: List<ChatMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_MINE = 1
        private const val VIEW_TYPE_OTHER = 2
    }

    // 내 메시지 ViewHolder (레이아웃에는 tvMessageDate 추가되어 있다고 가정)
    inner class MyMessageViewHolder(val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: ChatMessage, isFirstInGroup: Boolean, isLastInGroup: Boolean) {
            binding.tvMessageContent.text = chat.message
            // 그룹의 마지막 메시지인 경우에만 시간 표시
            if (isLastInGroup) {
                binding.tvMessageTime.text = chat.time
                binding.tvMessageTime.visibility = View.VISIBLE
            } else {
                binding.tvMessageTime.visibility = View.GONE
            }
            // 날짜 표시는 어댑터에서 처리합니다.
        }
    }

    // 상대방 메시지 ViewHolder
    inner class OtherMessageViewHolder(val binding: ItemMessageOtherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: ChatMessage, isFirstInGroup: Boolean, isLastInGroup: Boolean) {
            binding.tvMessageOtherContent.text = chat.message
            // 그룹의 첫 메시지인 경우 프로필 이미지와 이름 표시
            if (isFirstInGroup) {
                binding.tvMessageOtherName.text = chat.senderName
                binding.tvMessageOtherName.visibility = View.VISIBLE
                binding.ivMessageProfile.visibility = View.VISIBLE
            } else {
                binding.tvMessageOtherName.visibility = View.GONE
                binding.ivMessageProfile.visibility = View.GONE
            }
            // 그룹의 마지막 메시지인 경우에만 시간 표시
            if (isLastInGroup) {
                binding.tvMessageOtherTime.text = chat.time
                binding.tvMessageOtherTime.visibility = View.VISIBLE
            } else {
                binding.tvMessageOtherTime.visibility = View.GONE
            }
            // 날짜 표시는 어댑터에서 처리합니다.
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatList[position].isMine) VIEW_TYPE_MINE else VIEW_TYPE_OTHER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_MINE) {
            val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MyMessageViewHolder(binding)
        } else {
            val binding = ItemMessageOtherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            OtherMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = chatList[position]

        // 기존 그룹 처리
        val isFirstInGroup = if (position == 0) {
            true
        } else {
            val prevMessage = chatList[position - 1]
            !(prevMessage.isMine == message.isMine && prevMessage.time == message.time)
        }
        val isLastInGroup = if (position == chatList.size - 1) {
            true
        } else {
            val nextMessage = chatList[position + 1]
            !(nextMessage.isMine == message.isMine && nextMessage.time == message.time)
        }

        // 날짜 표시 여부: 첫 메시지이거나, 이전 메시지와 날짜가 다르면 표시
        val showDate = if (position == 0) {
            true
        } else {
            val prevMessage = chatList[position - 1]
            message.date != prevMessage.date
        }

        when (holder) {
            is MyMessageViewHolder -> {
                holder.bind(message, isFirstInGroup, isLastInGroup)
                // 내 메시지 레이아웃의 tvMessageDate에 날짜 표시
                if (showDate) {
                    holder.binding.tvMessageDate.text = message.date
                    holder.binding.tvMessageDate.visibility = View.VISIBLE
                } else {
                    holder.binding.tvMessageDate.visibility = View.GONE
                }
            }
            is OtherMessageViewHolder -> {
                holder.bind(message, isFirstInGroup, isLastInGroup)
                // 상대방 메시지 레이아웃의 tvMessageOtherDate에 날짜 표시
                if (showDate) {
                    holder.binding.tvMessageOtherDate.text = message.date
                    holder.binding.tvMessageOtherDate.visibility = View.VISIBLE
                } else {
                    holder.binding.tvMessageOtherDate.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemCount(): Int = chatList.size
}
