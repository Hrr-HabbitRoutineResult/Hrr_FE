package com.example.hrr_android

enum class NotificationType(val titleTemplate: String, val contentTemplate: String?) {
    CHALLENGE_REMINDER(
        "{challenge}",
        "{challenge} 챌린지 인증 마감이 {time_left} 남았습니다."
    ),
    FOLLOW_NOTIFICATION(
        "나를 팔로우 한 챌린저",
        "{user} 님이 회원님을 팔로우하기 시작하였습니다."
    ),
    COMMENT_ON_QUESTION(
        "내 인증에 댓글이 달렸어요",
        "{comment}"
    ),
    COMMENT_ON_POST(
        "내 게시글에 댓글이 달렸어요",
        "{comment}"
    ),
    NEW_QUESTION(
        "새로운 질문이 등록되었어요",
        "{challenge}에 새로운 질문 인증이 등록되었습니다."
    ),
    BADGE_AWARDED(
        "{badge} 뱃지를 획득했어요!",
        "새롭게 획득한 뱃지를 확인하러 갈까요?"
    ),
    WARNING_NOTIFICATION(
        "부실 인증 경고를 받았어요",
        "{challenge}에서 부실 인증 경고가 적립되었습니다."
    ),
    CHALLENGE_EXIT( // 내용이 필요 없는 알림
        "{challenge} 챌린지에서 퇴출되었습니다.",
        null
    )
}
