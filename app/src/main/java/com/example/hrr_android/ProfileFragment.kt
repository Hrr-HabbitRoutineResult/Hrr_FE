package com.example.hrr_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.hrr_android.databinding.FragmentProfileBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null            //뷰 바인딩
    private val binding get() = _binding!!
    private var selectedBadges = ArrayList<Badge>()                 //대표 뱃지 리스트
    private val userViewModel: UserViewModel by viewModels()
    private val profileCommon = ProfileCommon()     //공통 로직 인스턴스 생성

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        //ViewPager2 Adapter 연결
        profileCommon.setupViewPager(binding, requireActivity(), true)

        //팔로우 클릭 처리
        profileCommon.onFollowClicked(requireActivity(), binding.llProfileFollower, "follower")
        profileCommon.onFollowClicked(requireActivity(), binding.llProfileFollowing, "following")

        //클릭 이벤트 처리 설정
        initClickListener()

        // LiveData 관찰 (데이터가 변경될 때 자동 업데이트되도록 설정)
        userViewModel.profile.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                //Todo: 프로필 사진 바인딩
                binding.tvProfileUsername.text = it.nickname    // 이름
                binding.tvProfileLevel.text = it.level.toString()// 레벨
                binding.tvProfileFollowerCount.text = it.followerCount.toString()  // 팔로워 수
                binding.tvProfileFollowingCount.text = it.followingCount.toString() // 팔로잉 수
                //Todo: 뱃지 관련 바인딩
            }
        }

        userViewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }

        // 유저 데이터 로드
        userViewModel.loadProfile()

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

        // 설정 버튼 클릭 처리
        binding.ivProfileMenu.setOnClickListener {
            val intent = Intent(requireContext(), ProfileMoreActivity::class.java)
            intent.putExtra("type", "setting")
            startActivity(intent)
        }

        // 프로필 수정 모드
        binding.tvProfileEdit.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        // 대표 뱃지 클릭 시 뱃지 수정 화면으로 전환
        binding.llProfileBadge.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            intent.putExtra("clicked", "badge")
            startActivity(intent)

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initClickListener() {
        // 레벨 클릭 시 레벨 로드맵으로 전환
        binding.llProfileRank.setOnClickListener {
            val intent = Intent(requireContext(), LevelActivity::class.java)
            startActivity(intent)
        }
    }
}