package com.example.hrr_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.access.ValidUtils
import com.example.hrr_android.databinding.FragmentProfileChallengeBinding

class ProfileChallengeFragment : Fragment() {
    private var _binding: FragmentProfileChallengeBinding? = null       //뷰 바인딩
    private val binding get() = _binding!!
    private var participatingChallengeList = ArrayList<Challenge>()     //참가중인 챌린지 리스트
    private var completedChallengeList = ArrayList<Challenge>()         //최근 완주한 챌린지 리스트
    private val userViewModel: UserViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileChallengeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // LiveData 관찰 (데이터가 변경될 때 자동 업데이트되도록 설정)
        userViewModel.challengesOngoing.observe(viewLifecycleOwner) { result ->
            Log.d("asdf", "ViewModel에서 받은 데이터: $result") // 디버깅용 로그 추가

            result.onSuccess { challengeList ->
                challengeList.map { challenge ->
                    Challenge(
                        challenge.name,
                        ValidUtils.getDrawableResId(requireContext(), challenge.image),
                        isCertified = challenge.verification
                    )
                    // Todo: 이미지 처리 추가 예정
                }.let {
                    participatingChallengeList.clear()
                    participatingChallengeList.addAll(it)
                    binding.rvProfileParticipatingChallengeContent.adapter?.notifyDataSetChanged()

                    if (participatingChallengeList.isNotEmpty()) {
                        binding.clProfileParticipatingChallengeContentNo.visibility = View.GONE
                        binding.rvProfileParticipatingChallengeContent.visibility = View.VISIBLE
                    } else {
                        binding.clProfileParticipatingChallengeContentNo.visibility = View.VISIBLE
                        binding.rvProfileParticipatingChallengeContent.visibility = View.GONE
                    }
                }
            }.onFailure {
                Log.e("HomeFragment", "API 데이터 로드 실패: ${it.message}") // 실패 시 로그 출력
            }
        }

        // 참가중인 챌린지 데이터 로딩
        userViewModel.fetchChallengesOngoing()

        //참가중인 챌린지 더미 데이터 - 테스트 시 주석 해제 or 설정
//        participatingChallengeList.apply {
//            add(Challenge("토익 800점", R.drawable.img_english_book, isCertified = false))
//            add(Challenge("토익 900점 찍기. 쫄?", R.drawable.img_english_book, isCertified = true))
//            add(Challenge("열 자 제한 테스트", R.drawable.img_english_book, isCertified = true))
//        }

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

        //더보기 버튼 클릭 처리
        binding.llProfileCompletedChallengeTitle.setOnClickListener {
            val intent = Intent(requireContext(), ProfileMoreActivity::class.java)
            intent.putExtra("type", "challenge")
            startActivity(intent)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}