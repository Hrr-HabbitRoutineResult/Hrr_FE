package com.example.hrr_android

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hrr_android.community.ui.BoardActivity
import com.example.hrr_android.databinding.FragmentSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ViewBinding 초기화
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListener()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initClickListener() {
        // 계정 설정
        binding.llSettingAccount.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                // 현재 보여지고 있는 Fragment 숨기기
                parentFragmentManager.findFragmentById(R.id.fl_profile_more_fragment_container)?.let { hide(it) }

                // 새로운 Fragment 추가
                add(R.id.fl_profile_more_fragment_container, SettingAccountFragment())
                addToBackStack(null) // 뒤로 가기 지원
                commit()
            }

            (activity as? ProfileMoreActivity)?.setTitle("계정 설정")

        }

        // 알림 설정
        binding.llSettingNotice.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                // 현재 보여지고 있는 Fragment 숨기기
                parentFragmentManager.findFragmentById(R.id.fl_profile_more_fragment_container)?.let { hide(it) }

                // 새로운 Fragment 추가
                add(R.id.fl_profile_more_fragment_container, SettingNoticeFragment())
                addToBackStack(null) // 뒤로 가기 지원
                commit()
            }

            (activity as? ProfileMoreActivity)?.setTitle("알림")

        }

        // 댓글 단 게시글 이동
        binding.llSettingPostComment.setOnClickListener {
            navigateToBoardActivity("댓글 단 글", null, "CommunityTabContentFragment")
        }

        // 저장한 게시글 이동
        binding.llSettingPostSave.setOnClickListener {
            navigateToBoardActivity("저장한 글", null, "CommunityTabContentFragment")
        }

        // 찜한 챌린지 이동
        binding.llSettingChallengeLike.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                // 현재 보여지고 있는 Fragment 숨기기
                parentFragmentManager.findFragmentById(R.id.fl_profile_more_fragment_container)?.let { hide(it) }

                // 새로운 Fragment 추가
                add(R.id.fl_profile_more_fragment_container, SettingLikeChallengeFragment())
                addToBackStack(null) // 뒤로 가기 지원
                commit()
            }

            (activity as? ProfileMoreActivity)?.setTitle("찜한 챌린지")
        }

        // 댓글 단 챌린지 이동
        binding.llSettingChallengeComment.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                // 현재 보여지고 있는 Fragment 숨기기
                parentFragmentManager.findFragmentById(R.id.fl_profile_more_fragment_container)?.let { hide(it) }

                // 새로운 Fragment 추가
                add(R.id.fl_profile_more_fragment_container, SettingCommentChallengeFragment())
                addToBackStack(null) // 뒤로 가기 지원
                commit()
            }

            (activity as? ProfileMoreActivity)?.setTitle("댓글 단 글")
        }

        // 저장한 단 챌린지 이동
        binding.llSettingChallengeSave.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                // 현재 보여지고 있는 Fragment 숨기기
                parentFragmentManager.findFragmentById(R.id.fl_profile_more_fragment_container)?.let { hide(it) }

                // 새로운 Fragment 추가
                add(R.id.fl_profile_more_fragment_container, SettingSaveChallengeFragment())
                addToBackStack(null) // 뒤로 가기 지원
                commit()
            }

            (activity as? ProfileMoreActivity)?.setTitle("저장한 글")
        }

        // 차단한 사용자 목록 이동
        binding.llSettingBloacklist.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                // 현재 보여지고 있는 Fragment 숨기기
                parentFragmentManager.findFragmentById(R.id.fl_profile_more_fragment_container)?.let { hide(it) }

                // 새로운 Fragment 추가
                add(R.id.fl_profile_more_fragment_container, SettingBlockListFragment())
                addToBackStack(null) // 뒤로 가기 지원
                commit()
            }

            (activity as? ProfileMoreActivity)?.setTitle("차단한 사용자")
        }


    }

    private fun navigateToBoardActivity(baseCategory: String, subTitle: String?, fragment: String) {
        val intent = Intent(requireContext(), BoardActivity::class.java).apply {
            putExtra("baseCategory", baseCategory) // 타이틀
            putExtra("subTitle", subTitle ?: "")  // 서브타이틀 (없으면 공백)
            putExtra("fragment", fragment)        // 표시할 프래그먼트
        }
        startActivity(intent)
    }

}