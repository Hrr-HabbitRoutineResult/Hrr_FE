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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileChallengeBinding.inflate(inflater, container, false)

        //더미 데이터
        participatingChallengeList.apply {
            add(Challenge("토익 800점", R.drawable.img_english_book, false))
            add(Challenge("토익 900점", R.drawable.img_english_book, true))
        }

        //데이터 유무 판단하여 뷰 전환
        if(participatingChallengeList.size==0){
            binding.clProfileParticipatingChallengeContentNo.visibility = View.VISIBLE
            binding.rvProfileParticipatingChallengeContent.visibility = View.GONE
        }

        //RecyclerView Adapter 연결
        val challengeRVAdapter = ProfileParticipatingChallengeRVAdapter(participatingChallengeList)
        binding.rvProfileParticipatingChallengeContent.adapter = challengeRVAdapter
        binding.rvProfileParticipatingChallengeContent.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        return binding.root
    }

}