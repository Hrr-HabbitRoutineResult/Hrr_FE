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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.access.ValidUtils
import com.example.hrr_android.databinding.FragmentProfileChallengeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileChallengeFragment : Fragment(), OnCompletedChallengeClickListener {
    private var _binding: FragmentProfileChallengeBinding? = null       //뷰 바인딩
    private val binding get() = _binding!!
    private var participatingChallengeList = ArrayList<Challenge>()     //참가중인 챌린지 리스트
    private var completedChallengeList = ArrayList<Challenge>()         //최근 완주한 챌린지 리스트
    private val userViewModel: UserViewModel by activityViewModels()
    private val otherUserViewModel: OtherUserViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileChallengeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ownerId = arguments?.getInt("ownerId", 0)?:0
        val isMyChallenge = (ownerId==0)

        if (isMyChallenge){
            /*
            * 내 참가중인 챌린지 연동
            * */

            // LiveData 관찰 (데이터가 변경될 때 자동 업데이트되도록 설정)
            userViewModel.challengesOngoing.observe(viewLifecycleOwner) { result ->

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
            userViewModel.fetchChallengesOngoing()  // 참가중인 챌린지 데이터 로딩

            /*
            * 최근 완주한 챌린지 연동
            * */

            // LiveData 관찰 (데이터가 변경될 때 자동 업데이트되도록 설정)
            userViewModel.challengesEnd.observe(viewLifecycleOwner) { challenges ->
                challenges?.completedChallenges?.map{challengeEnd->
                    Challenge(
                        challengeEnd.name,
                        challengeEnd.imageUrl.toInt(),      // 이미지 처리 구현 전이라 오류 방지를 위해 임시로 Int로 전환해서 사용
                        challengeEnd.description)

                }.let {
                    completedChallengeList.apply {
                        clear()
                        if (it != null) {
                            addAll(it)
                        }

                        if (completedChallengeList.isNotEmpty()) {
                            binding.clProfileCompletedChallengeContentNo.visibility = View.GONE
                            binding.rvProfileCompletedChallengeContent.visibility = View.VISIBLE
                        } else {
                            binding.clProfileCompletedChallengeContentNo.visibility = View.VISIBLE
                            binding.rvProfileCompletedChallengeContent.visibility = View.GONE
                        }
                    }
                }
            }
            userViewModel.loadChallengesEnd()       // 최근 완주한 챌린지 로드
        }

        else{
            /*
            * 다른 유저의 참가중인 챌린지 연동
            * */

            // LiveData 관찰 (데이터가 변경될 때 자동 업데이트되도록 설정)
            otherUserViewModel.challengesOngoing.observe(viewLifecycleOwner) { result ->

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
            otherUserViewModel.fetchChallengesOngoing(ownerId)

            // LiveData 관찰 (데이터가 변경될 때 자동 업데이트되도록 설정)
            otherUserViewModel.challengesEnd.observe(viewLifecycleOwner) { challenges ->
                challenges?.completedChallenges?.map{challengeEnd->
                    Challenge(
                        challengeEnd.name,
                        challengeEnd.imageUrl.toInt(),      // 이미지 처리 구현 전이라 오류 방지를 위해 임시로 Int로 전환해서 사용
                        challengeEnd.description)

                }.let {
                    completedChallengeList.apply {
                        clear()
                        if (it != null) {
                            addAll(it)
                        }

                        if (completedChallengeList.isNotEmpty()) {
                            binding.clProfileCompletedChallengeContentNo.visibility = View.GONE
                            binding.rvProfileCompletedChallengeContent.visibility = View.VISIBLE
                        } else {
                            binding.clProfileCompletedChallengeContentNo.visibility = View.VISIBLE
                            binding.rvProfileCompletedChallengeContent.visibility = View.GONE
                        }
                    }
                }
            }
            otherUserViewModel.loadChallengesEnd(ownerId)       // 최근 완주한 챌린지 로드
        }

        userViewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                val errorToUser = when {
                    it.contains("IllegalStateException") -> "데이터를 불러오는 중 문제가 발생했습니다. 다시 시도해 주세요."
                    it.contains("JsonSyntaxException") -> "서버 응답이 올바르지 않습니다. 업데이트를 확인해 주세요."
                    it.contains("SocketTimeoutException") -> "서버 응답이 지연되고 있습니다. 잠시 후 다시 시도해 주세요."
                    it.contains("IOException") -> "네트워크 연결을 확인해 주세요."
                    else -> "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."
                }

                Toast.makeText(requireContext(), errorToUser, Toast.LENGTH_LONG).show()
                Log.e("ProfileFragmentVM", "오류 발생: $errorMsg")
            }
        }

        otherUserViewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                val errorToUser = when {
                    it.contains("IllegalStateException") -> "데이터를 불러오는 중 문제가 발생했습니다. 다시 시도해 주세요."
                    it.contains("JsonSyntaxException") -> "서버 응답이 올바르지 않습니다. 업데이트를 확인해 주세요."
                    it.contains("SocketTimeoutException") -> "서버 응답이 지연되고 있습니다. 잠시 후 다시 시도해 주세요."
                    it.contains("IOException") -> "네트워크 연결을 확인해 주세요."
                    else -> "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."
                }

                Toast.makeText(requireContext(), errorToUser, Toast.LENGTH_LONG).show()
                Log.e("ProfileFragmentVM", "오류 발생: $errorMsg")
            }
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
        val profileCompletedChallengeRVAdapter = ProfileCompletedChallengeRVAdapter(completedChallengeList, this)
        binding.rvProfileCompletedChallengeContent.adapter = profileCompletedChallengeRVAdapter
        binding.rvProfileCompletedChallengeContent.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        //더보기 버튼 클릭 처리
        binding.llProfileCompletedChallengeTitle.setOnClickListener {
            val intent = Intent(requireContext(), ProfileMoreActivity::class.java)
            intent.putExtra("type", "challenge")
            intent.putExtra("ownerId", ownerId)
            intent.putExtra("isMyProfile", false)
            startActivity(intent)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onChallengeClicked(challenge: Challenge) {
        findNavController().navigate(R.id.action_profileChallengeFragment_to_challengeFragment)
    }

}