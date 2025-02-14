package com.example.hrr_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.FragmentFollowerBinding
import com.example.hrr_android.databinding.ItemProfileFollowBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowerFragment : Fragment(), OnFollowClickListener {
    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!
    private var followerList = ArrayList<Follow>()
    private var followingIdList = ArrayList<Int>()
    private var currentOverlayPosition: Int = RecyclerView.NO_POSITION
    private val userViewModel: UserViewModel by activityViewModels()
    private var userIdToUnfollow: Int = 0
    private var followerLoadingCnt: Int = 0
    private var followingLoadingCnt: Int = 0
    private var userId: Int = 0     // 유저 아이디

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ViewBinding 초기화
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        * 팔로워, 팔로잉 리스트 연동
        * */

        userId = userViewModel.myId
        // LiveData 관찰 (데이터가 변경될 때 자동 업데이트되도록 설정)
        userViewModel.apply {
            // 팔로워 리스트
            followers.observe(viewLifecycleOwner) { response ->
                followerLoadingCnt++
                updateFollowerList(response)
            }

            // 팔로잉 리스트(나도 상대를 팔로우하고 있는지 판단하기 위함)
            followings.observe(viewLifecycleOwner) { response ->
                followingLoadingCnt++
                updateFollowingList(response)
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

        // 팔로우 데이터 로드
        userViewModel.loadFollowers(userId)
        userViewModel.loadFollowings(userId)

        //팔로워 RecyclerView 연결
        val followRVAdapter = FollowRVAdapter(followerList, this)
        binding.rvFollower.apply {
            adapter = followRVAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        // 언팔로우 메시지 클릭 처리
        binding.flUnfollowView.setOnClickListener {
            doUnfollow()    // 언팔로우 창 숨김
            userViewModel.unfollow(userId, userIdToUnfollow)    // 언팔로우 처리
            // 정보 업데이트
            followerList.find { it.id == userIdToUnfollow }?.let { follow ->
                follow.isFollowing = false
                binding.rvFollower.adapter?.notifyDataSetChanged()
            }
        }

        // 스크롤 리스너 추가
        setupScrollListener()

    }

    override fun onResume() {
        super.onResume()
        binding.rvFollower.adapter?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onFollowClicked(follow: Follow) {
        // 해당 유저 팔로우 시작 처리
        userViewModel.follow(userId, follow.id)
        // 정보 업데이트
        follow.isFollowing = true
        binding.rvFollower.adapter?.notifyDataSetChanged()
    }

    override fun onFollowingClicked(position: Int, follow: Follow) {
        // 언팔로우 메시지 세팅
        binding.tvUnfollow.text = "${follow.name} 님 언팔로우하기"

        // 언팔로우할 유저 id 할당
        userIdToUnfollow = follow.id

        // 언팔로우 메시지 뷰 이동
        showOverlayAt(position)

    }

    override fun onUserClicked(follow: Follow) {
        val intent = Intent(requireContext(), OtherProfileActivity::class.java).apply {
            putExtra("name", follow.name)   //Todo: 추후 API 연결 시 이름으로 사용자의 정보를 받아와 바인딩 예정
        }
        startActivity(intent)
    }

    private fun showOverlayAt(position: Int) {
        // 해당 아이템의 위치 찾아서 언팔로우 메시지 띄우기
        val unfollowMsg = binding.flUnfollowView
        val layoutManager = binding.rvFollower.layoutManager as LinearLayoutManager

        val itemView = layoutManager.findViewByPosition(position)

        if (itemView != null) {
            unfollowMsg.visibility = View.VISIBLE
            unfollowMsg.translationY = itemView.top.toFloat()
            currentOverlayPosition = position
        }
    }

    private fun setupScrollListener() {
        binding.rvFollower.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (binding.flUnfollowView.visibility == View.VISIBLE) {
                    updateOverlayPosition(currentOverlayPosition)
                }
            }
        })
    }

    private fun updateOverlayPosition(position: Int) {
        if (position == RecyclerView.NO_POSITION) return

        val layoutManager = binding.rvFollower.layoutManager as LinearLayoutManager
        val itemView = layoutManager.findViewByPosition(position)

        if (itemView != null) {
            binding.flUnfollowView.translationY = itemView.top.toFloat()
        } else {
            binding.flUnfollowView.visibility = View.GONE
        }
    }

    // 언팔로우 메시지를 숨기고 팔로우-팔로잉 아이콘을 교체
    private fun doUnfollow() {
        if (currentOverlayPosition == RecyclerView.NO_POSITION) return

        val layoutManager = binding.rvFollower.layoutManager as LinearLayoutManager

        val itemView = layoutManager.findViewByPosition(currentOverlayPosition)

        if (itemView != null) {
            // 버튼을 GONE으로 변경 (ViewBinding 활용)
            val binding = ItemProfileFollowBinding.bind(itemView)
            binding.ivFollowingBtn.visibility = View.GONE
            binding.ivFollowerBtn.visibility = View.VISIBLE
        }

        // Overlay 숨기기
        binding.flUnfollowView.visibility = View.GONE
        currentOverlayPosition = RecyclerView.NO_POSITION
    }

    // 팔로워 리스트 업데이트 함수
    private fun updateFollowerList(response: FollowResponse?) {
        response?.followers?.map{follower->
//                    val level = when(follower.level){
//                        "general" -> "일반"
//                        "bronze" -> "브론즈"
//                        "silver" -> "실버"
//                        "gold" -> "골드"
//                        "master" -> "마스터"
//                        "challenger" -> "챌린저"
//                        else -> ""
//                    }

            Follow(
                follower.id,
                follower.nickname,
                "일반",
                R.drawable.ic_profile_default,          //Todo: 이미지 처리 추가
                isFollower = true,
                isFollowing = followingIdList.contains(follower.id))    // 팔로잉 리스트에 해당 id가 있으면 true
        }.let {
            followerList.apply {
                clear()
                if (it != null) {
                    addAll(it)
                    if(followerLoadingCnt==1){
                        // 팔로워 정보 업데이트
                        followerList.forEach { follow ->
                            follow.isFollowing = follow.id in followingIdList
                        }
                        // 최초 데이터 로드 시에만 UI 업데이트 하도록 함
                        binding.rvFollower.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    // 팔로잉 리스트 업데이트 함수
    private fun updateFollowingList(response: FollowResponse?) {
        response?.followings?.map{followings->
            followings.id
        }.let {
            followingIdList.apply {
                clear()
                if (it != null) {
                    addAll(it)
                    if(followingLoadingCnt==1){
                        // 팔로워 정보 업데이트
                        followerList.forEach { follow ->
                            follow.isFollowing = follow.id in followingIdList
                        }
                        // 최초 데이터 로드 시에만 UI 업데이트 하도록 함
                        binding.rvFollower.adapter?.notifyDataSetChanged()
                    }

                }
            }
        }
    }



}