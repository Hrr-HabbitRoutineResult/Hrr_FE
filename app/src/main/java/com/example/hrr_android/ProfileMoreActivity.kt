package com.example.hrr_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hrr_android.databinding.ActivityProfileMoreBinding

class ProfileMoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileMoreBinding    // 뷰 바인딩

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //뷰 바인딩 초기화
        binding = ActivityProfileMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Intent에서 데이터 수신
        val type = intent.getStringExtra("type") ?: ""

        //클릭이 인식된 부분에 따라 뷰를 다르게 구성
        if (savedInstanceState == null) {
            //제목 변경
            binding.tvProfileMoreTitle.text = when(type){
                "challenge" -> "완주 챌린지"
                "certification" -> "전체 인증기록"
                "badge" -> "마이 뱃지"
                "follower" -> "프로필"
                "following" -> "프로필"
                "setting" -> "설정"
                else -> "완주 챌린지"        //기본적으로 챌린지 화면 사용
            }

            val fragment = when (type) {
                "challenge" -> ProfileChallengeMoreFragment()
                "certification" -> ProfileRecordMoreFragment()
                "badge" -> ProfileBadgeMoreFragment()
                "follower", "following" -> ProfileFollowFragment().apply {
                    arguments = Bundle().apply {
                        putString("selected_tab", type)
                    }
                }
                "setting" -> SettingFragment()
                else -> ProfileChallengeMoreFragment() // 기본적으로 ProfileChallengeMoreFragment 사용
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_profile_more_fragment_container, fragment)
                .commit()
        }

        //뒤로가기 버튼 클릭 처리
        binding.ivProfileMoreBack.setOnClickListener{
            finish()
        }

    }
}