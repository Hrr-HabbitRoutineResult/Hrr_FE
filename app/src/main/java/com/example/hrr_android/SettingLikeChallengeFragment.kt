package com.example.hrr_android

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.GridLayoutManager
import com.example.hrr_android.databinding.FragmentSettingLikeChallengeBinding

class SettingLikeChallengeFragment : Fragment() {
    private var _binding: FragmentSettingLikeChallengeBinding? = null
    private val binding get() = _binding!!
    private var completedChallenges = ArrayList<Challenge>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingLikeChallengeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (parentFragmentManager.backStackEntryCount >= 1) {
                        parentFragmentManager.popBackStack() // 추가 Fragment가 있을 때느 이전 Fragment로 돌아감

                        // 가장 최근에 추가된 Fragment를 다시 보이도록 설정
                        val currentFragment = parentFragmentManager.fragments.lastOrNull()
                        currentFragment?.let {
                            parentFragmentManager.beginTransaction().show(it).commit()
                        }
                    }

                    (activity as? ProfileMoreActivity)?.setTitle("설정")
                }
            })

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

        val profileChallengerMoreRVAdapter = ProfileChallengerMoreRVAdapter(completedChallenges, isSaved = true)
        binding.rvLikedChallenge.apply {
            adapter = profileChallengerMoreRVAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? ProfileMoreActivity)?.setTitle("설정")
        _binding = null
    }


}