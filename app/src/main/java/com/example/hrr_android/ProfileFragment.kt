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
    private var myProfile: UserResponse = UserResponse()    // 로딩된 사용자 정보

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //클릭 이벤트 처리 설정
        initClickListener()

        //ViewPager2 Adapter 연결
        profileCommon.setupViewPager(binding, requireActivity(), true)

        userViewModel.profile.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                myProfile = it  // 불러온 정보를 저장해놔서 다른 Fragment를 띄울 때 필요한 정보만 전달하여 불필요한 api 호출을 방지
                //Todo: 프로필 사진 바인딩
                binding.tvProfileUsername.text = it.nickname    // 이름
                binding.tvProfileLevel.text = when(it.level){
                    "general" -> "일반"
                    "bronze" -> "브론즈"
                    "silver" -> "실버"
                    "gold" -> "골드"
                    "master" -> "마스터"
                    "challenger" -> "챌린저"
                    else -> ""
                }
                binding.tvProfileFollowerCount.text = it.followerCount.toString()  // 팔로워 수
                binding.tvProfileFollowingCount.text = it.followingCount.toString() // 팔로잉 수
                //Todo: 뱃지 관련 바인딩
                profileCommon.setupCircularProgressBar(binding, myProfile.level, myProfile.points) // 레벨 달성률 게이지 바 구현
            }
        }

        userViewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                val errorToUser = when {
                    it.contains("IllegalStateException") -> "데이터를 불러오는 중 문제가 발생했습니다. 다시 시도해 주세요."
                    it.contains("JsonSyntaxException") -> "서버 응답이 올바르지 않습니다. 업데이트를 확인해 주세요."
                    it.contains("SocketTimeoutException") -> "서버 응답이 지연되고 있습니다. 잠시 후 다시 시도해 주세요."
                    it.contains("IOException") -> "네트워크 연결을 확인해 주세요."
                    else -> "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."
                }

                Toast.makeText(requireContext(), errorToUser, Toast.LENGTH_LONG).show()
                Log.e("ProfileFragmentVM", "오류 발생: $errorMsg")
            }
        }


        // 유저 데이터 로드
        userViewModel.loadProfile()

        //뱃지 더미 데이터 - 테스트 시 주석 해제 or 설정
        selectedBadges.clear()

        selectedBadges.apply {
            add(Badge("프로 챌린저", R.drawable.badge_type_fromtoday_challenger))
            add(Badge("수준급 스터디언", R.drawable.badge_type_fromtoday_challenger))
            add(Badge("운동 스타터", R.drawable.badge_type_fromtoday_challenger))
        }

        //설정한 대표 뱃지 개수에 따라 visibility 조정
        profileCommon.setupBadges(binding, selectedBadges)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initClickListener() {
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

        // 레벨 클릭 시 레벨 로드맵으로 전환
        binding.llProfileRank.setOnClickListener {
            val intent = Intent(requireContext(), LevelActivity::class.java)
            intent.putExtra("point", myProfile.points)
            startActivity(intent)
        }

        //팔로우 클릭 처리
        profileCommon.onFollowClicked(requireActivity(), binding.llProfileFollower, "follower")
        profileCommon.onFollowClicked(requireActivity(), binding.llProfileFollowing, "following")

        // 대표 뱃지 클릭 시 뱃지 수정 화면으로 전환
        binding.llProfileBadge.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            intent.putExtra("clicked", "badge")
            startActivity(intent)

        }
    }
}