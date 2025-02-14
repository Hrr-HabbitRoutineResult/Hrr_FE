package com.example.hrr_android

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hrr_android.databinding.FragmentProfileFollowBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFollowFragment : Fragment() {
    private var _binding: FragmentProfileFollowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ViewBinding 초기화
        _binding = FragmentProfileFollowBinding.inflate(inflater, container, false)

        //ViewPager2 Adapter 연결
        val ownerId = arguments?.getInt("ownerId", 0)?: 0
        Log.d("otherDebug", "ProfileFollowFragment - $ownerId")
        val followVPAdapter = FollowVPAdapter(this, ownerId)
        binding.vpFollow.adapter = followVPAdapter

        //탭 제목 설정
        TabLayoutMediator(binding.tlFollow, binding.vpFollow){
                tab, position ->tab.text = if (position == 0) "팔로워" else "팔로잉"
        }.attach()

        // 전달받은 탭 정보 가져오기
        val selectedTab = when(arguments?.getString("selected_tab", "follower")){
            "follower" -> 0
            "following" -> 1
            else -> 0
        }

        // 해당 탭으로 이동
        binding.vpFollow.setCurrentItem(selectedTab, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}