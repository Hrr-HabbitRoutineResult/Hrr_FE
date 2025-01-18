package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.databinding.FragmentProfileChallengeBinding

class ProfileChallengeFragment : Fragment() {
    private lateinit var binding: FragmentProfileChallengeBinding
    private var participatingChallengeList = ArrayList<Challenge>()
    private var completedChallengeList = ArrayList<Challenge>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileChallengeBinding.inflate(inflater, container, false)

        //더미 데이터
        participatingChallengeList.apply {
            add(Challenge("토익 800점", R.drawable.img_english_book, isCertified = false))
            add(Challenge("토익 900점 찍기. 쫄?", R.drawable.img_english_book, isCertified = true))
            add(Challenge("열 자 제한 테스트", R.drawable.img_english_book, isCertified = true))
        }
        completedChallengeList.apply {
            add(Challenge("흑백 요리사 나가실 분", R.drawable.img_cook, "흑백요리사 시즌 4쯤에 나가는 걸 목표로"))
            add(Challenge("백종원 따라잡기", R.drawable.img_cook, "흑백요리사 시즌 400쯤에 나가는 걸 목표로"))
            add(Challenge("테스트 데이터입니다", R.drawable.img_cook, "120자 제한이니까 좀 많이 늘린다고 하면 아마 넘어가지 않을까요? 근데 쓰기 귀찮으니까 좀만 쓸게요"))
        }

        //데이터 유무 판단하여 뷰 전환
        if(participatingChallengeList.size==0){
            binding.clProfileParticipatingChallengeContentNo.visibility = View.VISIBLE
            binding.rvProfileParticipatingChallengeContent.visibility = View.GONE
        }

        if(completedChallengeList.size==0){
            binding.clProfileCompletedChallengeContentNo.visibility = View.VISIBLE
            binding.rvProfileCompletedChallengeContent.visibility = View.GONE
        }


        //RecyclerView Adapter 연결
        val profileParticipatingChallengeRVAdapter = ProfileParticipatingChallengeRVAdapter(participatingChallengeList)
        binding.rvProfileParticipatingChallengeContent.adapter = profileParticipatingChallengeRVAdapter
        binding.rvProfileParticipatingChallengeContent.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val profileCompletedChallengeRVAdapter = ProfileCompletedChallengeRVAdapter(completedChallengeList)
        binding.rvProfileCompletedChallengeContent.adapter = profileCompletedChallengeRVAdapter
        binding.rvProfileCompletedChallengeContent.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        return binding.root
    }

}