package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hrr_android.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding            //뷰 바인딩
    private var selectedBadges = ArrayList<Badge>()                 //대표 뱃지 리스트
    private val profileCommon = ProfileCommon()     //공통 로직 인스턴스 생성

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        //ViewPager2 Adapter 연결
        profileCommon.setupViewPager(binding, requireActivity(), true)

        //팔로우 클릭 처리
        profileCommon.onFollowClicked(requireActivity(), binding.llProfileFollower, "follower")
        profileCommon.onFollowClicked(requireActivity(), binding.llProfileFollowing, "following")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //레벨 달성률 게이지 바 구현
        profileCommon.setupCircularProgressBar(binding, 76, 100)

        //뱃지 더미 데이터 - 테스트 시 주석 해제 or 설정
        selectedBadges.clear()

        selectedBadges.apply {
            add(Badge("프로 챌린저", R.drawable.img_badge_challenge_01))
            add(Badge("수준급 스터디언", R.drawable.img_badge_challenge_01))
            add(Badge("운동 스타터", R.drawable.img_badge_challenge_01))
        }

        //설정한 대표 뱃지 개수에 따라 visibility 조정
        profileCommon.setupBadges(binding, selectedBadges)


    }

}