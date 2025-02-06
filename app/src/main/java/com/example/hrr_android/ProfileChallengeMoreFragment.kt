package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.databinding.FragmentProfileChallengeMoreBinding

class ProfileChallengeMoreFragment : Fragment() {
    //뷰 바인딩
    private var _binding: FragmentProfileChallengeMoreBinding? = null
    private val binding get() = _binding!!
    //챌린지 리스트
    private var completedChallenges = ArrayList<Challenge>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileChallengeMoreBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //챌린지 더미 데이터
        completedChallenges.apply {
            add(Challenge("흑백 요리사 나가실 분", R.drawable.img_cook, "흑백요리사 시즌 4쯤에 나가는 걸 목표로"))
            add(Challenge("백종원 따라잡기", R.drawable.img_cook, "흑백요리사 시즌 400쯤에 나가는 걸 목표로"))
            add(Challenge("챌린지명 열 자 제한", R.drawable.img_cook, "설명은 120자 제한이니까 좀 많이 늘린다고 하면 아마 넘어가지 않을까요? 근데 쓰기 귀찮으니까 좀만 쓸게요"))
            add(Challenge("챌린지 4", R.drawable.img_cook, "테스트"))
            add(Challenge("챌린지 5", R.drawable.img_cook, "테스트"))
            add(Challenge("챌린지 6", R.drawable.img_cook, "테스트"))
            add(Challenge("챌린지 7", R.drawable.img_cook, "테스트"))
            add(Challenge("챌린지 8", R.drawable.img_cook, "테스트"))
            add(Challenge("챌린지 9", R.drawable.img_cook, "테스트"))
            add(Challenge("챌린지 10", R.drawable.img_cook, "테스트"))
        }

        val profileChallengerMoreRVAdapter = ProfileChallengerMoreRVAdapter(completedChallenges)
        binding.rvChallengeMore.apply {
            adapter = profileChallengerMoreRVAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}