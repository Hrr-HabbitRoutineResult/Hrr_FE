package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.databinding.FragmentSettingSaveChallengeBinding

class SettingSaveChallengeFragment : Fragment(), OnRecordClickListener {
    private var _binding: FragmentSettingSaveChallengeBinding? = null
    private val binding get() = _binding!!
    private var certifications = ArrayList<Certification>() //기록 리스트

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingSaveChallengeBinding.inflate(inflater, container, false)

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

        // 더미 데이터
        certifications.apply {
            add(Certification("챌린지명", "게시글 제목", "2024.12.31", R.drawable.img_running, true))
            add(Certification("Run To You", "마지막 인증합니다~^^", "2024.12.31", R.drawable.img_running, true))
            add(Certification("사진 없을 경우", "게시글 제목", "2025.01.01", hasLink = false))
            add(Certification("링크 없을 경우", "1년 만에 인증합니다~^^", "2025.01.01", R.drawable.img_running, false))
            add(Certification("인증 제목 없을 경우", "", "2025.01.01", R.drawable.img_running, false))
            add(Certification("테스트", "게시글 제목", "2024.12.31",R.drawable.img_running, true))
            add(Certification("테스트", "게시글 제목", "2024.12.31",R.drawable.img_running, true))
            add(Certification("테스트", "게시글 제목", "2024.12.31",R.drawable.img_running, true))
            add(Certification("테스트", "게시글 제목", "2024.12.31",R.drawable.img_running, true))
            add(Certification("테스트", "게시글 제목", "2024.12.31",R.drawable.img_running, true))
            add(Certification("테스트", "게시글 제목", "2024.12.31",R.drawable.img_running, true))
            add(Certification("테스트", "게시글 제목", "2024.12.31",R.drawable.img_running, true))
        }

        // Adapter 연결
        val profileRecordMoreRVAdapter = ProfileRecordMoreRVAdapter(certifications, this)
        binding.rvPostSaved.apply {
            adapter = profileRecordMoreRVAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? ProfileMoreActivity)?.setTitle("설정")
        _binding = null
    }

    override fun onRecordClicked(certification: Certification) {
        TODO("Not yet implemented")
    }
}