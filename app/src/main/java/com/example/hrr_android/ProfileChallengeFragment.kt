package com.example.hrr_android

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.databinding.FragmentProfileChallengeBinding

class ProfileChallengeFragment : Fragment() {
    private lateinit var binding: FragmentProfileChallengeBinding       //뷰 바인딩
    private var participatingChallengeList = ArrayList<Challenge>()     //참가중인 챌린지 리스트
    private var completedChallengeList = ArrayList<Challenge>()         //최근 완주한 챌린지 리스트

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileChallengeBinding.inflate(inflater, container, false)

        //참가중인 챌린지 더미 데이터 - 테스트 시 주석 해제 or 설정
        participatingChallengeList.apply {
            add(Challenge("토익 800점", R.drawable.img_english_book, isCertified = false))
            add(Challenge("토익 900점 찍기. 쫄?", R.drawable.img_english_book, isCertified = true))
            add(Challenge("열 자 제한 테스트", R.drawable.img_english_book, isCertified = true))
        }

        //최근 완주한 챌린지 더미 데이터 - 테스트 시 주석 해제 or 설정
        completedChallengeList.apply {
            add(Challenge("흑백 요리사 나가실 분", R.drawable.img_cook, "흑백요리사 시즌 4쯤에 나가는 걸 목표로"))
            add(Challenge("백종원 따라잡기", R.drawable.img_cook, "흑백요리사 시즌 400쯤에 나가는 걸 목표로"))
            add(Challenge("챌린지명 열 자 제한", R.drawable.img_cook, "설명은 120자 제한이니까 좀 많이 늘린다고 하면 아마 넘어가지 않을까요? 근데 쓰기 귀찮으니까 좀만 쓸게요"))
        }

        //데이터 유무 판단하여 뷰 전환
        if(participatingChallengeList.size != 0){
            //참가중인 챌린지가 있을 때
            binding.clProfileParticipatingChallengeContentNo.visibility = View.GONE
            binding.rvProfileParticipatingChallengeContent.visibility = View.VISIBLE
        }

        if(completedChallengeList.size != 0){
            //최근 완주한 챌린지가 있을 때
            binding.clProfileCompletedChallengeContentNo.visibility = View.GONE
            binding.rvProfileCompletedChallengeContent.visibility = View.VISIBLE
        }

        //참가중인 챌린지 RecyclerView Adapter 연결
        val profileParticipatingChallengeRVAdapter = ProfileParticipatingChallengeRVAdapter(participatingChallengeList)
        binding.rvProfileParticipatingChallengeContent.adapter = profileParticipatingChallengeRVAdapter
        binding.rvProfileParticipatingChallengeContent.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        //최근 완주한 챌린지 RecyclerView Adapter 연결
        val profileCompletedChallengeRVAdapter = ProfileCompletedChallengeRVAdapter(completedChallengeList)
        binding.rvProfileCompletedChallengeContent.adapter = profileCompletedChallengeRVAdapter
        binding.rvProfileCompletedChallengeContent.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        //더보기 버튼 클릭 리스터
        binding.llProfileCompletedChallengeMore.setOnClickListener {
            val intent = Intent(requireContext(), ProfileMoreActivity::class.java)
            intent.putExtra("type", "challenge")
            startActivity(intent)
        }

        return binding.root
    }

}