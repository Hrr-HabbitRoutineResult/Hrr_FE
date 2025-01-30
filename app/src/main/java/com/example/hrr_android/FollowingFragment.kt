package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.FragmentFollowingBinding

class FollowingFragment : Fragment(), OnFollowClickListener {
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private var followingList = ArrayList<Follow>()
    private var currentOverlayPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ViewBinding 초기화
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)

        //팔로워 더미 데이터
        followingList.apply {
            add(Follow("김흐르", "실버", R.drawable.ic_profile_default, false, true))
            add(Follow("김흐르", "실버", R.drawable.ic_profile_default, false, true))
            add(Follow("김흐르", "실버", R.drawable.ic_profile_default, false, true))
            add(Follow("김흐르", "실버", R.drawable.ic_profile_default, true, true))
            add(Follow("조흐르", "실버", R.drawable.ic_profile_default, true, true))
            add(Follow("최흐르", "실버", R.drawable.ic_profile_default, true, true))
            add(Follow("김흐르", "실버", R.drawable.ic_profile_default, true, true))
            add(Follow("김흐르", "실버", R.drawable.ic_profile_default, true, true))
            add(Follow("김흐르", "실버", R.drawable.ic_profile_default, true, true))
            add(Follow("김흐르", "실버", R.drawable.ic_profile_default, true, true))
        }

        //팔로워 RecyclerView 연결
        val followRVAdapter = FollowRVAdapter(followingList, this)
        binding.rvFollowing.apply {
            adapter = followRVAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        // 스크롤 리스너 추가
        setupScrollListener()

        return binding.root
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

        // 해당 유저 팔로우 해제 처리
        showOverlayAt(position)     //언팔로우 메시지 뷰 이동


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
}