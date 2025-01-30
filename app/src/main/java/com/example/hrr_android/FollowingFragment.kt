package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.databinding.FragmentFollowingBinding

class FollowingFragment : Fragment(), OnFollowClickListener {
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private var followingList = ArrayList<Follow>()

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
            add(Follow("김흐르", "실버", R.drawable.ic_profile_default, true, true))
            add(Follow("김흐르", "실버", R.drawable.ic_profile_default, true, true))
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

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onFollowClicked(follow: Follow) {
        // "팔로잉" 화면에서는 내가 팔로우 하는 사용자만 나오기 때문에 아이콘이 모두 "팔로잉" 상태이므로 구현 해당 없음
    }

    override fun onFollowingClicked(follow: Follow) {
        // 해당 유저 팔로우 해제 처리
    }
}