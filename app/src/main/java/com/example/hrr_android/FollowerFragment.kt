package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.databinding.FragmentFollowerBinding

class FollowerFragment : Fragment(), OnFollowClickListener {
    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!
    private var followerList = ArrayList<Follow>()

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
            add(Follow("김흐르", "실버", R.drawable.ic_profile_default, true, true))
            add(Follow("김흐르", "실버", R.drawable.ic_profile_default, true, true))
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

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onFollowClicked(follow: Follow) {
        // 해당 유저 팔로우 시작 처리
    }

    override fun onFollowingClicked(follow: Follow) {
        // 해당 유저 팔로우 해제 처리
    }

}