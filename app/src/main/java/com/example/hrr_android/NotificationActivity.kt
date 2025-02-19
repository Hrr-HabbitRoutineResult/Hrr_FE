package com.example.hrr_android

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding  // 뷰 바인딩 적용

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

/*        val notificationList = listOf(
            Notification(
                NotificationType.CHALLENGE_REMINDER,
                mapOf("challenge" to "자잘자잘"),
                TimeLeft.ONE_HOUR,
                "1분 전",
                R.drawable.img_running
            ),
            Notification(
                NotificationType.FOLLOW_NOTIFICATION,
                mapOf("user" to "김철수"),
                null,
                "5분 전",
                R.drawable.ic_notification_follow
            ),
            Notification(
                NotificationType.COMMENT_ON_QUESTION,
                mapOf("comment" to "해당 코드는 복잡한 형태라서 지금 사용하신 방법보다는 제가 링크 달아드릴 테니까 이런 식으로 진행해보시는 게 어떨까요?"),
                null,
                "1시간 전",
                R.drawable.img_study
            ),
            Notification(
                NotificationType.CHALLENGE_REMINDER,
                mapOf("challenge" to "자잘자잘"),
                TimeLeft.THREE_HOURS,
                "4시간 전",
                R.drawable.img_running
            ),
            Notification(
                NotificationType.COMMENT_ON_POST,
                mapOf("comment" to "나 그 챌린지 참여하고 싶은데 챌린지 개설해줄 수 있을까? 쪽지 보내놓을 테니까 나중에 연락 줘!"),
                null,
                "5시간 전",
                R.drawable.ic_notification_profile
            ),
            Notification(
                NotificationType.BADGE_AWARDED,
                mapOf("badge" to "운동 마스터"),
                null,
                "6시간 전",
                R.drawable.ic_notification_badge
            ),
            Notification(
                NotificationType.NEW_QUESTION,
                mapOf("challenge" to "자잘자잘"),
                null,
                "8시간 전",
                R.drawable.img_running
            ),
            Notification(
                NotificationType.CHALLENGE_EXIT,
                mapOf("challenge" to "매일매일코딩"),
                null,
                "10시간 전",
                R.drawable.ic_notification_warning
            ),
            Notification(
                NotificationType.WARNING_NOTIFICATION,
                mapOf("challenge" to "매일매일코딩"),
                null,
                "1일 전",
                R.drawable.ic_notification_warning
            )
        )*/

        // 더미 데이터 없는 상태
        val notificationList = listOf<Notification>()

        // RecyclerView 설정
        binding.rvNotificationList.layoutManager = LinearLayoutManager(this)
        binding.rvNotificationList.adapter = NotificationRVAdapter(notificationList)

        // 리스트가 비어 있는지 확인 후 UI 변경
        if (notificationList.isEmpty()) {
            binding.rvNotificationList.visibility = View.GONE
            binding.layoutNotificationListNone.visibility = View.VISIBLE
        } else {
            binding.rvNotificationList.visibility = View.VISIBLE
            binding.layoutNotificationListNone.visibility = View.GONE
        }

        // 뒤로가기
        binding.btnNotificationBack.setOnClickListener {
            finish()
        }

    }
}
