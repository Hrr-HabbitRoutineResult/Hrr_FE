package com.example.hrr_android

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.FragmentFollowerBinding
import com.example.hrr_android.databinding.ItemProfileFollowBinding

class FollowerFragment : Fragment(), OnFollowClickListener {
    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!
    private var followerList = ArrayList<Follow>()
    private var currentOverlayPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ViewBinding 초기화
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)

        //팔로워 더미 데이터
        followerList.apply {
            add(Follow("김흐르", "실버", R.drawable.ic_profile_default, true, false))
            add(Follow("김흐르", "실버", R.drawable.ic_profile_default, true, false))
            add(Follow("김흐르", "실버", R.drawable.ic_profile_default, true, false))
            add(Follow("김흐르", "실버", R.drawable.ic_profile_default, true, false))
            add(Follow("조흐르", "실버", R.drawable.ic_profile_default, true, true))
            add(Follow("이흐르", "실버", R.drawable.ic_profile_default, true, true))
            add(Follow("김흐르", "실버", R.drawable.ic_profile_default, true, true))
            add(Follow("김흐르", "실버", R.drawable.ic_profile_default, true, true))
            add(Follow("김흐르", "실버", R.drawable.ic_profile_default, true, true))
            add(Follow("김흐르", "실버", R.drawable.ic_profile_default, true, true))
        }

        //팔로워 RecyclerView 연결
        val followRVAdapter = FollowRVAdapter(followerList, this)
        binding.rvFollower.apply {
            adapter = followRVAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        // 언팔로우 메시지 클릭 처리
        binding.flUnfollowView.setOnClickListener {
            doUnfollow()
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
        // 해당 유저 팔로우 시작 처리
    }

    override fun onFollowingClicked(position: Int, follow: Follow) {
        //언팔로우 메시지 세팅
        binding.tvUnfollow.text = "${follow.name} 님 언팔로우하기"

        // 해당 유저 팔로우 해제 처리
        showOverlayAt(position)     //언팔로우 메시지 뷰 이동


    }

    override fun onUserClicked(follow: Follow) {
        val intent = Intent(requireContext(), OtherProfileActivity::class.java).apply {
            putExtra("name", follow.name)   //추후 API 연결 시 이름으로 사용자의 정보를 받아와 바인딩 예정
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

}