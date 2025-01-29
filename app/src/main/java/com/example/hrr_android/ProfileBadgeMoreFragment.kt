package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.hrr_android.databinding.FragmentProfileBadgeMoreBinding

class ProfileBadgeMoreFragment : Fragment() {
    //뷰 바인딩
    private var _binding: FragmentProfileBadgeMoreBinding? = null
    private val binding get() = _binding!!
    //뱃지 리스트
    private var typeBadgeList = ArrayList<Badge>()
    private var categoryBadgeList = ArrayList<Badge>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBadgeMoreBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //유형 뱃지 더미 데이터
        typeBadgeList.apply {
            add(Badge("뱃지명", R.drawable.img_badge_challenge_01))
            add(Badge("오늘부터 챌린저", R.drawable.img_badge_challenge_01))
            add(Badge("뱃지 이름이 이렇게 길어지면 어떻게 될까요", R.drawable.img_badge_challenge_01))
            add(Badge("뱃지명", R.drawable.img_badge_challenge_01))
            add(Badge("뱃지명", R.drawable.img_badge_challenge_01))
        }

        //유형 뱃지 RecyclerView 연결
        val typeBadgeMoreRVAdapter = ProfileBadgeMoreRVAdapter(typeBadgeList)
        binding.rvBadgeMoreType.apply {
            adapter = typeBadgeMoreRVAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }

        //카테고리 뱃지 더미 데이터
        categoryBadgeList.apply {
            add(Badge("뱃지명", R.drawable.img_badge_health_03))
            add(Badge("프로 운동러", R.drawable.img_badge_health_03))
            add(Badge("뱃지 이름이 이렇게 길어지면 어떻게 될까요", R.drawable.img_badge_health_03))
            add(Badge("뱃지명", R.drawable.img_badge_health_03))
            add(Badge("뱃지명", R.drawable.img_badge_health_03))
        }

        //카테고리 뱃지 RecyclerView 연결
        val categoryBadgeMoreRVAdapter = ProfileBadgeMoreRVAdapter(categoryBadgeList)
        binding.rvBadgeMoreCategory.apply {
            adapter = categoryBadgeMoreRVAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}