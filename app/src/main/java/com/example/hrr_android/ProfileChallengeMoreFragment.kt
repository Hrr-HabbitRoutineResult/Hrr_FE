package com.example.hrr_android

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.hrr_android.access.ValidUtils
import com.example.hrr_android.databinding.FragmentProfileChallengeMoreBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileChallengeMoreFragment : Fragment(), OnCompletedChallengeClickListener {
    //뷰 바인딩
    private var _binding: FragmentProfileChallengeMoreBinding? = null
    private val binding get() = _binding!!
    //챌린지 리스트
    private var completedChallenges = ArrayList<Challenge>()
    private val userViewModel: UserViewModel by activityViewModels()
    private val otherUserViewModel: OtherUserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileChallengeMoreBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ownerId = arguments?.getInt("ownerId", 0)?:0
        val isMyChallenge = (ownerId==0)

        if (isMyChallenge){
            /*
            * 완료한 챌린지 데이터 연동
            * */

            // LiveData 관찰 (데이터가 변경될 때 자동 업데이트되도록 설정)
            userViewModel.challengesEnd.observe(viewLifecycleOwner) { challenges ->
                challenges?.completedChallenges?.map{challengeEnd->
                    Challenge(
                        challengeEnd.name,
                        0,      // 이미지 처리 구현 전이라 오류 방지를 위해 임시로 Int로 전환해서 사용
                        challengeEnd.description)

                }.let {
                    completedChallenges.apply {
                        clear()
                        if (it != null) {
                            addAll(it)
                            binding.rvChallengeMore.adapter?.notifyDataSetChanged()
                        }
                    }
                }
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

            // 유저 데이터 로드
            userViewModel.loadChallengesEnd()
        }

        else{
            /*
            * 완료한 챌린지 데이터 연동
            * */

            // LiveData 관찰 (데이터가 변경될 때 자동 업데이트되도록 설정)
            otherUserViewModel.challengesEnd.observe(viewLifecycleOwner) { challenges ->
                challenges?.completedChallenges?.map{challengeEnd->
                    Challenge(
                        challengeEnd.name,
                        0,      // 이미지 처리 구현 전이라 오류 방지를 위해 임시로 Int로 전환해서 사용
                        challengeEnd.description)

                }.let {
                    completedChallenges.apply {
                        clear()
                        if (it != null) {
                            addAll(it)
                            binding.rvChallengeMore.adapter?.notifyDataSetChanged()
                        }
                    }
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

            // 유저 데이터 로드
            otherUserViewModel.loadChallengesEnd(ownerId)
        }

        val profileChallengerMoreRVAdapter = ProfileChallengerMoreRVAdapter(completedChallenges, listener = this)
        binding.rvChallengeMore.apply {
            adapter = profileChallengerMoreRVAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onChallengeClicked(challenge: Challenge) {
        val bundle = Bundle().apply {
            putString("state", "completed")
        }

        //findNavController().navigate(R.id.action_profileChallengeMoreFragment_to_challengeFragment, bundle)
    }
}