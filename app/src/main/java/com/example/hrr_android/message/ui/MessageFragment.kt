package com.example.hrr_android.message.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.message.model.MessageList
import com.example.hrr_android.message.adapter.MessageListRVAdapter
import com.example.hrr_android.R
import com.example.hrr_android.databinding.FragmentMessageBinding

class MessageFragment : Fragment() {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 더미 데이터 리스트 생성
        val messageList = listOf(
            MessageList(R.drawable.ic_message_profile_default, "kimheureu", "안녕하세요, 게시물 보고 연락드렸어요.", "50분 전"),
            MessageList(R.drawable.ic_message_profile_default, "김흐르르르", "스터디 챌린지 혹시 개설하셨을까요??", "1시간 전"),
            MessageList(R.drawable.ic_message_profile_default, "최흐르", "챌린지 이름 좀 알려주실 수 있을까요", "6시간 전"),
            MessageList(R.drawable.ic_message_profile_default, "챌린저명", "Text", "1일 전"),
            MessageList(R.drawable.ic_message_profile_default, "챌린저명", "Text", "2일 전"),
            MessageList(R.drawable.ic_message_profile_default, "챌린저명", "Text", "3일 전"),
            MessageList(R.drawable.ic_message_profile_default, "챌린저명", "Text", "5일 전"),
            MessageList(R.drawable.ic_message_profile_default, "챌린저명", "Text", "6일 전"),
        )

        // RecyclerView 설정 & 아이템 클릭 리스너 추가
        binding.rvMessageList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMessageList.adapter = MessageListRVAdapter(messageList) {
            // 클릭 시 MessageChatActivity로 이동
            val intent = Intent(requireContext(), MessageChatroomActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수 방지
    }
}
