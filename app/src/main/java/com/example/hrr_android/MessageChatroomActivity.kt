package com.example.hrr_android

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.databinding.ActivityMessageChatroomBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageChatroomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageChatroomBinding
    private lateinit var chatAdapter: ChatroomRVAdapter
    private val chatList = mutableListOf<ChatMessage>() // 채팅 메시지 리스트

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMessageChatroomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView 설정
        chatAdapter = ChatroomRVAdapter(chatList)
        binding.rvMessageChat.layoutManager = LinearLayoutManager(this)
        binding.rvMessageChat.adapter = chatAdapter

        // 더미 데이터 추가
        addDummyMessages()

        // 햄버거 버튼 클릭 시 DrawerLayout의 오른쪽(side sheet) 열기
        binding.btnMessageChatMore.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }

        // include된 레이아웃의 내부 닫기 버튼 클릭 시 DrawerLayout 닫기
        binding.sheetMessageBottom.btnMessageBottomClose.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
        }

        // include된 레이아웃의 내부 나가기 버튼 클릭 시 다이얼로그 닫기
        binding.sheetMessageBottom.tvMessageBottomExit.setOnClickListener {
            val dialog = DefaultDialog(
                this,
                "나가기",
                "해당 쪽지방을 나가시겠어요?",
                object : DefaultDialog.DialogListener {
                    override fun onYesClicked() {
                        Toast.makeText(this@MessageChatroomActivity, "나가기 선택됨", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    override fun onNoClicked() {
                        Toast.makeText(this@MessageChatroomActivity, "취소됨", Toast.LENGTH_SHORT).show()
                    }
                }
            )
            dialog.show()
        }

        // include된 레이아웃의 내부 나가기 버튼 클릭 시 다이얼로그 닫기
        binding.sheetMessageBottom.tvMessageBottomBlock.setOnClickListener {
            val dialog = DefaultDialog(
                this,
                "차단",
                "이 챌린저와의 쪽지 수신 및 발신이 차단되며\n차단된 챌린저는 회원님의 프로필과 \n게시한 글도 볼 수 없게 됩니다.",
                object : DefaultDialog.DialogListener {
                    override fun onYesClicked() {
                        Toast.makeText(this@MessageChatroomActivity, "나가기 선택됨", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    override fun onNoClicked() {
                        Toast.makeText(this@MessageChatroomActivity, "취소됨", Toast.LENGTH_SHORT).show()
                    }
                }
            )
            dialog.show()
        }

        // 뒤로 가기 버튼 클릭 시 Activity 종료
        binding.btnMessageChatBack.setOnClickListener {
            finish()
        }

        // 전송 버튼 클릭 시 메시지 추가
        binding.btnMessageChatSend.setOnClickListener {
            sendMessage()
        }

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(keyboardVisibilityListener)
    }

    private fun addDummyMessages() {
        chatList.add(ChatMessage("챌린저명", "안녕하세요", "오후 2:41", false, "2025-01-31"))
        chatList.add(ChatMessage("챌린저명", "챌린지 때문에 연락 드렸어요", "오후 2:41", false, "2025-01-31"))
        chatList.add(ChatMessage("나", "넵", "오후 2:43", true, "2025-01-31"))
        chatList.add(ChatMessage("챌린저명", "저도 참가할 수 있을까요?", "오후 2:43", false, "2025-02-01"))
        chatList.add(ChatMessage("나", "좋아요!", "오후 2:43", true, "2025-02-01"))
        chatList.add(ChatMessage("나", "열심히 해봐요", "오후 2:43", true, "2025-02-01"))

        chatAdapter.notifyDataSetChanged() // 데이터 변경을 RecyclerView에 반영
    }

    private fun sendMessage() {
        val messageText = binding.etMessageChat.text.toString().trim()
        if (messageText.isNotEmpty()) {
            // 현재 시간을 "오전/오후 h:mm" 형식으로 가져오기
            val currentTime = SimpleDateFormat("a h:mm", Locale.getDefault()).format(Date())
            // 현재 날짜를 "yyyy-MM-dd" 형식으로 가져오기
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            val newMessage = ChatMessage("나", messageText, currentTime, true, currentDate)
            chatList.add(newMessage)
            val newIndex = chatList.size - 1

            // 이전 메시지가 같은 그룹이라면, 이전 메시지가 더 이상 그룹의 마지막이 아니게 됨
            if (newIndex > 0) {
                val previousMessage = chatList[newIndex - 1]
                if (previousMessage.isMine == newMessage.isMine && previousMessage.time == newMessage.time) {
                    chatAdapter.notifyItemChanged(newIndex - 1)
                }
            }

            // 새 메시지가 추가된 부분을 갱신
            chatAdapter.notifyItemInserted(newIndex)

            // ScrollView를 최하단으로 스크롤
            binding.scrollMessageChat.post {
                binding.scrollMessageChat.fullScroll(View.FOCUS_DOWN)
            }

            // 입력 필드 초기화
            binding.etMessageChat.text?.clear()

            // EditText 자동 클릭 및 키보드 표시
            binding.etMessageChat.post {
                binding.etMessageChat.performClick()
                binding.etMessageChat.requestFocus()

                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.etMessageChat, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }


    private val keyboardVisibilityListener = ViewTreeObserver.OnGlobalLayoutListener {
        val rect = Rect()
        binding.root.getWindowVisibleDisplayFrame(rect)
        val screenHeight = binding.root.height
        val keypadHeight = screenHeight - rect.bottom

        val params = binding.etMessageChat.layoutParams as ViewGroup.MarginLayoutParams

        if (keypadHeight > screenHeight * 0.15) { // 키보드가 나타났을 때
            params.bottomMargin = keypadHeight - 120 // EditText 위치 조정

            binding.rvMessageChat.post {
                val layoutManager = binding.rvMessageChat.layoutManager as LinearLayoutManager
                layoutManager.scrollToPositionWithOffset(chatList.size - 1, 0) // 키보드가 올라왔을 때 스크롤
            }
        } else { // 키보드가 사라졌을 때
            params.bottomMargin = 0
        }

        binding.etMessageChat.layoutParams = params
    }
}


