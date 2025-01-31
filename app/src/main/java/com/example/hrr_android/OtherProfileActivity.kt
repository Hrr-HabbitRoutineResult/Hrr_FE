package com.example.hrr_android

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.hrr_android.databinding.ActivityOtherProfileBinding
import com.example.hrr_android.databinding.FragmentProfileBinding

class OtherProfileActivity : AppCompatActivity() {
    //뷰 바인딩
    private lateinit var binding: ActivityOtherProfileBinding       //뷰 바인딩
    private lateinit var profileBinding: FragmentProfileBinding     //include로 불러온 뷰 사용을 위한 바인딩
    private val profileCommon = ProfileCommon()     //공통 로직 인스턴스 생성
    private var selectedBadges = ArrayList<Badge>()                 //대표 뱃지 리스트

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOtherProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // include된 뷰의 바인딩 초기화 - View 타입 객체가 필요하여 뷰 바인딩이 아닌 findViewById 방식을 사용
        val includedView = findViewById<View>(R.id.include_profile_fragment)
        profileBinding = FragmentProfileBinding.bind(includedView)

        // 더미 데이터
        selectedBadges.apply {
            add(Badge("프로 챌린저", R.drawable.img_badge_challenge_01))
            add(Badge("수준급 스터디언", R.drawable.img_badge_challenge_01))
            add(Badge("운동 스타터", R.drawable.img_badge_challenge_01))
        }

        // 안 쓰는 뷰들 정리
        profileBinding.ivProfileLogo.visibility = View.GONE
        profileBinding.ivProfileMenu.visibility = View.GONE
        profileBinding.tvProfileEdit.visibility = View.GONE

        // 전환해서 사용하는 뷰 숨기기
        binding.flUnfollowView.visibility = View.GONE
        binding.ivOtherFollowing.visibility = View.GONE

        // ViewPager 연결
        profileCommon.setupViewPager(profileBinding, this, false)

        // 레벨 달설 바 연결
        profileCommon.setupCircularProgressBar(profileBinding, 76, 100)

        // 대표 뱃지 바인딩
        profileCommon.setupBadges(profileBinding, selectedBadges)

        // 팔로우 목록 클릭 처리 - TODO: 팔로우 상세 화면을 Activity로 변경하면 구현 예정. 현재는 Fragment를 띄울 화면이 없어 바인딩 불가
//        profileCommon.onFollowClicked(this, profileBinding.llProfileFollower, "follower")
//        profileCommon.onFollowClicked(this, profileBinding.llProfileFollowing, "following")

        // 팔로우 or 팔로잉 클릭 처리
        binding.ivOtherFollow.setOnClickListener {
            //"팔로우" 상태 아이콘 클릭 시
            binding.ivOtherFollow.visibility = View.GONE
            binding.ivOtherFollowing.visibility = View.VISIBLE    //팔로우 시작
        }
        binding.ivOtherFollowing.setOnClickListener {
            //"팔로잉" 상태 아이콘 클릭 시
            binding.flUnfollowView.visibility = View.VISIBLE
        }
        binding.flUnfollowView.setOnClickListener {
            binding.flUnfollowView.visibility = View.GONE
            binding.ivOtherFollow.visibility = View.VISIBLE
            binding.ivOtherFollowing.visibility = View.GONE    //팔로우 해제

        }

        // 뒤로 가기 버튼 클릭 처리
        binding.ivOtherProfileBack.setOnClickListener {
            finish()
        }


    }
}





