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
import com.example.hrr_android.databinding.FragmentFollowingBinding
import com.example.hrr_android.databinding.ItemProfileFollowBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowingFragment() : Fragment(), OnFollowClickListener {
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private var followingList = ArrayList<Follow>()
    private var currentOverlayPosition: Int = RecyclerView.NO_POSITION
    private val userViewModel: UserViewModel by activityViewModels()
    private var userIdToUnfollow: Int = 0
    private var followingLoadingCnt: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ViewBinding 초기화
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        * 팔로잉 리스트 연동
        * */

        // LiveData 관찰 (데이터가 변경될 때 자동 업데이트되도록 설정)
        userViewModel.followings.observe(viewLifecycleOwner) { response ->
            followingLoadingCnt++
            updateFollowingList(response)
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
        userViewModel.loadFollowings()

        //팔로워 RecyclerView 연결
        val followRVAdapter = FollowRVAdapter(followingList, this)
        binding.rvFollowing.apply {
            adapter = followRVAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        // 언팔로우 메시지 클릭 처리
        binding.flUnfollowView.setOnClickListener {
            doUnfollow()
            userViewModel.unfollow(userIdToUnfollow)    // 언팔로우 처리
        }

        // 스크롤 리스너 추가
        setupScrollListener()

    }

    override fun onResume() {
        super.onResume()
        binding.rvFollowing.adapter?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onFollowClicked(follow: Follow) {
        // "팔로잉" 화면에서는 내가 팔로우 하는 사용자만 나오기 때문에 아이콘이 모두 "팔로잉" 상태이므로 구현 해당 없음
    }

    override fun onFollowingClicked(position: Int, follow: Follow) {
        //언팔로우 메시지 세팅
        binding.tvUnfollow.text = "${follow.name} 님 언팔로우하기"

        // 언팔로우할 유저 id 할당
        userIdToUnfollow = follow.id

        // 해당 유저 팔로우 해제 처리
        showOverlayAt(position)     //언팔로우 메시지 뷰 이동


    }

    override fun onUserClicked(follow: Follow) {
        val intent = Intent(requireContext(), OtherProfileActivity::class.java).apply {
            putExtra("id", follow.id)
        }
        startActivity(intent)
    }

    private fun showOverlayAt(position: Int) {
        // 해당 아이템의 위치 찾아서 언팔로우 메시지 띄우기
        val unfollowMsg = binding.flUnfollowView
        val layoutManager = binding.rvFollowing.layoutManager as LinearLayoutManager

        val itemView = layoutManager.findViewByPosition(position)

        if (itemView != null) {
            unfollowMsg.visibility = View.VISIBLE
            unfollowMsg.translationY = itemView.top.toFloat()
            currentOverlayPosition = position
        }
    }

    private fun setupScrollListener() {
        binding.rvFollowing.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

        val layoutManager = binding.rvFollowing.layoutManager as LinearLayoutManager
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

        val layoutManager = binding.rvFollowing.layoutManager as LinearLayoutManager

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

    private fun updateFollowingList(response: FollowResponse?){
        response?.followings?.map{following->
//                    val level = when(following.level){
//                        "general" -> "일반"
//                        "bronze" -> "브론즈"
//                        "silver" -> "실버"
//                        "gold" -> "골드"
//                        "master" -> "마스터"
//                        "challenger" -> "챌린저"
//                        else -> ""
//                    }

            Follow(
                following.id,
                following.nickname,
                "일반",
                R.drawable.ic_profile_default,          //Todo: 이미지 처리 추가
                isFollowing = true)
        }.let {
            followingList.apply {
                clear()
                if (it != null) {
                    addAll(it)
                    if(followingLoadingCnt==1){
                        // 최초 데이터 로드 시에만 UI 업데이트 하도록 함
                        binding.rvFollowing.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }

}