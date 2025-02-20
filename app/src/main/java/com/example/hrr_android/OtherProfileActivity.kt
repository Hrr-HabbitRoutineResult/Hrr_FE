package com.example.hrr_android

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.hrr_android.databinding.ActivityOtherProfileBinding
import com.example.hrr_android.databinding.DialogBottomSheetBinding
import com.example.hrr_android.databinding.FragmentProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherProfileActivity : AppCompatActivity() {
    //뷰 바인딩
    private lateinit var binding: ActivityOtherProfileBinding       //뷰 바인딩
    private lateinit var profileBinding: FragmentProfileBinding     //include로 불러온 뷰 사용을 위한 바인딩
    private val profileCommon = ProfileCommon()     //공통 로직 인스턴스 생성
    private var selectedBadges = ArrayList<Badge>()                 //대표 뱃지 리스트
    private var userId:Int = 0
    private val userViewModel: UserViewModel by viewModels()
    private val otherUserViewModel: OtherUserViewModel by viewModels()
    private var myProfile: UserResponse = UserResponse()    // 로딩된 사용자 정보

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userId = intent.getIntExtra("id", 0)    // 넘겨받은 id를 배정
        Log.d("otherDebug", "OtherProfileActivity - $userId")

        binding = ActivityOtherProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // include된 뷰의 바인딩 초기화 - View 타입 객체가 필요하여 뷰 바인딩이 아닌 findViewById 방식을 사용
        val includedView = findViewById<View>(R.id.include_profile_fragment)
        profileBinding = FragmentProfileBinding.bind(includedView)

        // 유저 정보 바인딩
        otherUserViewModel.profile.observe(this) { profile ->
            profile?.let {
                myProfile = it  // 불러온 정보를 저장해놔서 다른 Fragment를 띄울 때 필요한 정보만 전달하여 불필요한 api 호출을 방지
                userId = it.id?:0
                //Todo: 프로필 사진 바인딩
                profileBinding.tvProfileUsername.text = it.nickname    // 이름
                profileBinding.tvProfileLevel.text = when(it.level){
                    "general" -> "일반"
                    "bronze" -> "브론즈"
                    "silver" -> "실버"
                    "gold" -> "골드"
                    "master" -> "마스터"
                    "challenger" -> "챌린저"
                    else -> ""
                }
                profileBinding.tvProfileFollowerCount.text = it.followerCount.toString()  // 팔로워 수
                profileBinding.tvProfileFollowingCount.text = it.followingCount.toString() // 팔로잉 수
                //Todo: 뱃지 관련 바인딩
                it.level?.let { it1 ->
                    profileCommon.setupCircularProgressBar(profileBinding,
                        it1, it.points)
                } // 레벨 달성 바 연결
            }
        }

        otherUserViewModel.errorMessage.observe(this) { errorMsg ->
            errorMsg?.let {
                val errorToUser = when {
                    it.contains("IllegalStateException") -> "데이터를 불러오는 중 문제가 발생했습니다. 다시 시도해 주세요."
                    it.contains("JsonSyntaxException") -> "서버 응답이 올바르지 않습니다. 업데이트를 확인해 주세요."
                    it.contains("SocketTimeoutException") -> "서버 응답이 지연되고 있습니다. 잠시 후 다시 시도해 주세요."
                    it.contains("IOException") -> "네트워크 연결을 확인해 주세요."
                    else -> "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."
                }

                Toast.makeText(this, errorToUser, Toast.LENGTH_LONG).show()
                Log.e("ProfileFragmentVM", "오류 발생: $errorMsg")
            }
        }

        // 유저 데이터 로드
        otherUserViewModel.loadProfile(userId)

        // 팔로잉하는 사람인지 확인
        userViewModel.followings.observe(this) { followingList ->
            val isFollowing = followingList?.followings?.any { it.id == myProfile.id } ?: false
            
            if (isFollowing) {
                binding.ivOtherFollow.visibility = View.GONE
                binding.ivOtherFollowing.visibility = View.VISIBLE
            } else {
                binding.ivOtherFollow.visibility = View.VISIBLE
                binding.ivOtherFollowing.visibility = View.GONE
            }
        }
        userViewModel.loadFollowings()  // 계정 주인의 팔로잉 목록 불러오기


        // 더미 데이터
        selectedBadges.apply {
            add(Badge("프로 챌린저", R.drawable.badge_type_fromtoday_challenger))
            add(Badge("수준급 스터디언", R.drawable.badge_type_fromtoday_challenger))
            add(Badge("운동 스타터", R.drawable.badge_type_fromtoday_challenger))
        }

        // 안 쓰는 뷰들 정리
        profileBinding.ivProfileLogo.visibility = View.GONE
        profileBinding.ivProfileMenu.visibility = View.GONE
        profileBinding.tvProfileEdit.visibility = View.GONE
        profileBinding.ivRankMore.visibility = View.GONE

        // 전환해서 사용하는 뷰 숨기기
        binding.flUnfollowView.visibility = View.GONE
        binding.ivOtherFollowing.visibility = View.GONE

        // ViewPager 연결
        profileCommon.setupViewPager(profileBinding, this, false, userId)

        // 대표 뱃지 바인딩
        profileCommon.setupBadges(profileBinding, selectedBadges)

        // 팔로우 목록 클릭 처리
        val myId = intent.getIntExtra("myId", 0)
        profileCommon.onFollowClicked(this, profileBinding.llProfileFollower, "follower", false, userId, myId)
        profileCommon.onFollowClicked(this, profileBinding.llProfileFollowing, "following", false, userId, myId)

        // 팔로우 or 팔로잉 클릭 처리
        binding.ivOtherFollow.setOnClickListener {
            //"팔로우" 상태 아이콘 클릭 시
            binding.ivOtherFollow.visibility = View.GONE
            binding.ivOtherFollowing.visibility = View.VISIBLE
            //팔로우 시작
            userViewModel.follow(myProfile.id?: 0)
        }
        binding.ivOtherFollowing.setOnClickListener {
            //"팔로잉" 상태 아이콘 클릭 시
            binding.flUnfollowView.visibility = View.VISIBLE
        }
        binding.flUnfollowView.setOnClickListener {
            binding.flUnfollowView.visibility = View.GONE
            binding.ivOtherFollow.visibility = View.VISIBLE
            binding.ivOtherFollowing.visibility = View.GONE
            //팔로우 해제
            userViewModel.unfollow(myProfile.id?: 0)

        }

        // 뒤로 가기 버튼 클릭 처리
        binding.ivOtherProfileBack.setOnClickListener {
            finish()
        }

        // 차단, 신고 버튼 클릭 처리
        binding.ivOtherProfileMore.setOnClickListener {
            val dialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
            val dialogBinding = DialogBottomSheetBinding.inflate(layoutInflater) // 뷰 바인딩 객체 생성

            dialog.setContentView(dialogBinding.root)

            // 버튼 클릭 리스너 설정
            dialogBinding.tvBlock.setOnClickListener {
                Toast.makeText(this, "차단이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                userViewModel.blockUser(userId)
                dialog.dismiss()
            }

            dialogBinding.tvReport.setOnClickListener {
                Toast.makeText(this, "신고하기 선택됨", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            dialogBinding.tvCancel.setOnClickListener {
                Toast.makeText(this, "취소 선택됨", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            dialog.show()
        }


    }
}





